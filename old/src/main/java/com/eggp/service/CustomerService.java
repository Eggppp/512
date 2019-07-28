package com.eggp.service;

import com.eggp.model.Customer;
import com.eggp.smart.annotation.Transaction;
import com.eggp.util.DatabaseUtil;
import com.eggp.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 客户数据服务层
 */
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    /**
     * 获取客户列表
     * @return
     */
    public List<Customer> getCustomerList(){
        String sql = "select * from customer";
        return DatabaseUtil.queryEntityList(Customer.class,sql);
        /*List<Customer> customerList = new ArrayList<>();
        try {
            String sql = "select * from customer";
            connection= DatabaseUtil.getConnection();
            *//*PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Customer customer = new Customer();
                customer.setId(resultSet.getLong("id"));
                customer.setName(resultSet.getString("name"));
                customerList.add(customer);
            }*//*

            return customerList=DatabaseUtil.queryEntityList(Customer.class,connection,sql);
        }*//*catch (SQLException e){
            LOGGER.error("execute sql failure",e);
        }*//*finally {
            DatabaseUtil.closeConnection(connection);
        }*/
        // return customerList;
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public Customer getCustomer(long id){
        String sql = "select * from customer where id = " +id;
        return DatabaseUtil.queryEntity(Customer.class,sql);
    }

    /**
     * 创建用户
     * @param fieldMap
     * @return
     */
    @Transaction
    public boolean createCustomer(Map<String,Object> fieldMap){
        return DatabaseUtil.insertEntity(Customer.class,fieldMap);
    }

    /**
     * 更新用户
     * @param id
     * @param fieldMap
     * @return
     */
    @Transaction
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return DatabaseUtil.updateEntity(Customer.class,id,fieldMap);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Transaction
    public boolean deleteCustomer(long id){
        return DatabaseUtil.deleteEntity(Customer.class,id);
    }



}
