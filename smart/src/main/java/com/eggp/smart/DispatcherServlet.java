package com.eggp.smart;

import com.eggp.smart.bean.Data;
import com.eggp.smart.bean.Handler;
import com.eggp.smart.bean.Param;
import com.eggp.smart.bean.View;
import com.eggp.smart.helper.BeanHelper;
import com.eggp.smart.helper.ConfigHelper;
import com.eggp.smart.helper.ControllerHelper;
import com.eggp.smart.helper.RequestHelper;
import com.eggp.smart.helper.ServletHelper;
import com.eggp.smart.helper.UploaderHelper;
import com.eggp.smart.util.JsonUtil;
import com.eggp.smart.util.ReflectionUtil;
import com.eggp.smart.util.StringUtil;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        HelperLoader.init();
        // 获取ServletContext对象，用于注册Servlet
        ServletContext servletContext=servletConfig.getServletContext();
        // 注册处理JSP的Servlet
        ServletRegistration jspServlet= servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        // 注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletHelper.init(req,resp);
        try {
            String requestMethod=req.getMethod().toLowerCase();
            String requestPath=req.getPathInfo();

            if(requestPath.equals("favicon.ico")){
                return;
            }

            Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
            if(handler!=null){
                Class<?> controllerClass=handler.getControllerClass();
                Object controllerBean= BeanHelper.getBean(controllerClass);

                Param param;
                if(UploaderHelper.isMultipart(req)){
                    param=UploaderHelper.createParam(req);
                }else {
                    param= RequestHelper.createParam(req);
                }

                Object result;
                Method actionMethod=handler.getActionMethod();
                if(param.isEmpty()){
                    result= ReflectionUtil.invokeMethod(controllerBean,actionMethod);
                }else {
                    result=ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
                }


                if(result instanceof View){
                    handleViewResult((View) result,req,resp);
                }else if(result instanceof Data){
                    handleDataResult((Data) result,resp);
                }
            /*

            Map<String,Object> paramMap=new HashMap<>();
            Enumeration<String> paramNames=req.getParameterNames();

            while (paramNames.hasMoreElements()){
                String paramName=paramNames.nextElement();
                String paramValue=req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body= CodeUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                String[] params=body.split("&");
                if(ArrayUtil.isNotEmpty(params)){
                    for(String param:params){
                        String[] array=param.split("=");
                        if(ArrayUtil.isNotEmpty(array)&&array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }

            Param param=new Param(paramMap);
            // 调用Action方法
            Method actionMethod=handler.getActionMethod();
            Object result= ReflectionUtil.invokeMethod(controllreBean,actionMethod,param);
            // 处理Action方法返回值
            if(result instanceof View){

            }else if(result instanceof Data){
                // 返回JSON
                Data data=(Data) result;

            }*/

            }
        }finally {
            ServletHelper.destory();
        }
    }

    private void handleViewResult(View view,HttpServletRequest request,HttpServletResponse response)
        throws IOException,ServletException{
        String path=view.getPath();
        if(StringUtil.isNotEmpty(path)){
            if(path.startsWith("/")){
                response.sendRedirect(request.getContextPath()+path);
            }else {
                Map<String ,Object> model=view.getModel();
                for(Map.Entry<String,Object>entry:model.entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
            }
        }
    }

    private void handleDataResult(Data data,HttpServletResponse response)
        throws IOException{
        Object model=data.getModel();
        if(model!=null){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json= JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }

    }

}
