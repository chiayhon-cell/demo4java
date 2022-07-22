package cn.chiayhon.bo;

import cn.chiayhon.exception.BizException;
import cn.chiayhon.properties.DatabaseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 默认的数据库连接管理类
 */
public class DefaultConnectionManager implements ConnectionManager {

    /**
     * @param databaseProperties
     * @return
     */
    public Connection getConnection(DatabaseProperties databaseProperties) {
        final String url = databaseProperties.getUrl();
        final String username = databaseProperties.getUsername();
        final String password = databaseProperties.getPassword();
        Connection connection;
        try {
            Class.forName(databaseProperties.getDatabase().getDriverClass());
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new BizException("加载jdbc驱动时异常", e);
        } catch (SQLException e) {
            throw new BizException("获取数据库连接时异常", e);
        }
        return connection;
    }
}
