package cn.chiayhon.bo;

import cn.chiayhon.properties.DatabaseProperties;

import java.sql.Connection;

/**
 * 数据库连接管理器
 */
public interface ConnectionManager {

    /**
     * 获取连接
     *
     * @param databaseProperties 数据库配置
     * @return 数据库连接
     */
    Connection getConnection(DatabaseProperties databaseProperties);


}
