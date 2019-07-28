package com.eggp.controller;

import com.eggp.model.Customer;
import com.eggp.service.CustomerService;
import com.eggp.smart.annotation.Action;
import com.eggp.smart.annotation.Controller;
import com.eggp.smart.annotation.Inject;
import com.eggp.smart.bean.Data;
import com.eggp.smart.bean.Param;
import com.eggp.smart.bean.View;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @Action("get:/customer")
    public View index(Param param){
        List<Customer>  customerList=customerService.getCustomerList();
        return new View("customer.jsp").addModel("customerList",customerList);
    }

    @Action("get:/customer_show")
    public View show(Param param){
        long id =param.getLong("id");
        Customer customer =customerService.getCustomer(id);
        return new View("customer_show.jsp").addModel("customer",customer);
    }

    /**
     * 进入创建客户界面
     * @param param
     * @return
     */
    @Action("get:/customer_create")
    public View create(Param param){
        return new View("customer_create.jsp");
    }

    /**
     * 处理创建客户请求
     * @param param
     * @return
     */
    @Action("post:/customer_create")
    public Data createSubmit(Param param){
        Map<String,Object> fieldMap=param.getMap();
        boolean result=customerService.createCustomer(fieldMap);
        return new Data(result);
    }

    /**
     * 进入编辑客户界面
     * @param param
     * @return
     */
    @Action("get:/customer_edit")
    public View edit(Param param){
        long id =param.getLong("id");
        Customer customer=customerService.getCustomer(id);
        return new View("customer_edit.jsp").addModel("customer",customer);
    }

    /**
     * 处理编辑客户请求
     * @param param
     * @return
     */
    @Action("post:/customer_edit")
    public Data editSubmit(Param param){
        long id =param.getLong("id");
        Map<String,Object> fieldMap=param.getMap();
        boolean result=customerService.updateCustomer(id,fieldMap);
        return new Data(result);
    }

    /**
     * 处理编辑客户请求
     * @param param
     * @return
     */
    @Action("delete:/customer_edit")
    public Data delete(Param param){
        long id =param.getLong("id");
        boolean result=customerService.deleteCustomer(id);
        return new Data(result);
    }



}
