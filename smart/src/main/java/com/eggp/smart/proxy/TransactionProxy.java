package com.eggp.smart.proxy;

import com.eggp.smart.annotation.Transaction;
import com.eggp.smart.bean.Data;
import com.eggp.smart.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.DataBuffer;
import java.lang.reflect.Method;

public class TransactionProxy implements Proxy{

    private static final Logger LOGGER= LoggerFactory.getLogger(TransactionProxy.class);

    /**
     *  本地线程变量，是一个标志，可以保证同一线程中事务控制相关逻辑只会执行一次。
     */
    private static final ThreadLocal<Boolean> FLAG_HOLDER=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag=FLAG_HOLDER.get();
        Method method=proxyChain.getTargetMethod();
        if(!flag&&method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result=proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            }finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result=proxyChain.doProxyChain();
        }
        return result;
    }
}
