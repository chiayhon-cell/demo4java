package cn.chiayhon.rest.service;

import cn.chiayhon.enumerate.DatabaseType;
import cn.chiayhon.properties.DatabaseProperties;
import cn.chiayhon.util.database.DatabaseInitProcessor;

import java.sql.Connection;

/**
 * 数据库操作类,负责底层api的封装
 */
public interface DatabaseAPIService {


    /**
     * 获取对应数据库的连接对象
     *
     * @param databaseProperties 数据库配置
     * @return 连接对象
     */
    Connection getConnection(DatabaseProperties databaseProperties);

    /**
     * 获取对应数据库的初始化处理器
     *
     * @param databaseType 数据库类型
     * @return 初始化处理器
     */
    DatabaseInitProcessor getInitProcessor(DatabaseType databaseType);

    DatabaseInitProcessor getInitProcessor(Class<DatabaseInitProcessor> aClass);


}
