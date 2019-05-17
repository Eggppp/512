package com.eggp.util;

import com.sun.org.apache.bcel.internal.generic.DASTORE;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库工具类
 */
public final class DatabaseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);

    private static final QueryRunner QUERY_RUNNER;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final BasicDataSource DATA_SOURCE;

    static {

        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER=new QueryRunner();

        Properties conf=PropertiesUtil.loadProperties("src/main/resources/dbconfig.properties");
        String driver=conf.getProperty("jdbc.driver");
        String url=conf.getProperty("jdbc.url");
        String username=conf.getProperty("jdbc.username");
        String password=conf.getProperty("jdbc.password");

        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);

    }

    public static Connection getConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if(connection==null){
            try {
                connection= DATA_SOURCE.getConnection();
            }catch (SQLException e){
                LOGGER.error("get connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    public static void closeConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if(connection!=null){
            try {
                connection.close();
            }catch (SQLException e){
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     * @param entityCLass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityCLass,String sql,Object...params){
        List<T> entityList;
        try {
            Connection connection=getConnection();
            entityList=QUERY_RUNNER.query(connection,sql,new BeanListHandler<T>(entityCLass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw  new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object...params){
        T entity;
        try{
            Connection connection=getConnection();
            entity=QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw  new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return  entity;
    }

    /**
     * 执行查询语句
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> executeQuery(String sql,Object...params){
        List<Map<String,Object>> result;
        try {
            Connection connection=getConnection();
            result=QUERY_RUNNER.query(connection,sql,new MapListHandler(),params);
        }catch (Exception e){
            LOGGER.error("query query failure",e);
            throw  new RuntimeException(e);
        }
        return result;
    }

    /**
     * 执行更新语句（包括update、insert、delete）
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql,Object...params){
        int rows=0;
        try{
            Connection connection=getConnection();
            rows=QUERY_RUNNER.update(connection,sql,params);
        }catch (SQLException e){
            LOGGER.error("query update failure",e);
            throw  new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity : fieldMap is empty");
            return false;
        }
        String sql ="INSERT INTO "+getTableName(entityClass);
        StringBuilder columns=new StringBuilder("(");
        StringBuilder values=new StringBuilder("(");
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql+= columns+"VALUES "+values;
        Object[] params=fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 更新实体
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity : fieldMap is empty");
            return false;
        }
        String sql ="UPDATE "+getTableName(entityClass)+" SET ";
        StringBuilder columns=new StringBuilder();
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql+= columns.substring(0,columns.lastIndexOf(", "))+" WHERE id=?";
        List<Object> paramList=new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params=paramList.toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "DELETE FROM " + getTableName(entityClass)+ " WHERE id=?";
        return executeUpdate(sql,id)==1;
    }

    /**
     * 获取表名
     * @param entityClass
     * @return
     */
    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    /**
     * 执行sql文件
     * @param filePath
     */
    public static void executeSqlFile(String filePath){
        InputStream inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        try {
            String sql;
            while ((sql=bufferedReader.readLine())!=null){
                DatabaseUtil.executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException(e);
        }

    }

}
