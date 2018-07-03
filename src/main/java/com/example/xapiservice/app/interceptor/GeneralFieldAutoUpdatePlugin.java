package com.example.xapiservice.app.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Date;
import java.util.Properties;


@Intercepts({@Signature(type = StatementHandler.class,method = "prepare", args = {Connection.class,Integer.class})})
@Component
public class GeneralFieldAutoUpdatePlugin implements Interceptor {

    private Logger log = LoggerFactory.getLogger(GeneralFieldAutoUpdatePlugin.class);
    @Value("${generalField}")
    private String generalField;

    /**
     * 拦截二次封装
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(
                statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory()
        );
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = statementHandler.getBoundSql();
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        String newSql = createGenerateFieldUpdateSql(boundSql.getSql(), commandType);
        metaObject.setValue("delegate.boundSql.sql", newSql);
        log.info("mybatis intercept sql:{}", newSql);
        return invocation.proceed();
    }

    private String createGenerateFieldUpdateSql(String sql, SqlCommandType commandType) throws NoSuchFieldException, IllegalAccessException {
        sql = sql.toLowerCase();
        if (commandType == SqlCommandType.INSERT) {
            sql = sql.replaceAll("\\)\\s*values", ", " + generalField + ") values ");
            //批量插入 语句修改
            sql = sql.replaceAll("\\)\\s*,\\s*\\(", ", " + new Date().getTime() + "),(");
            //将最后一个)替换为, 时间戳 )
            int index = sql.lastIndexOf(")");
            return sql.substring(0, index) + ", " + new Date().getTime() + " )";
        } else if (commandType == SqlCommandType.UPDATE){
            String generalFieldSql = ", " + generalField + "=" + new Date().getTime() ;
            if (sql.contains("where")) {
                sql = sql.replaceFirst("where", generalFieldSql + " where ");
            } else {
                sql += generalFieldSql;
            }
            return sql;
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //doing nothing
    }

}