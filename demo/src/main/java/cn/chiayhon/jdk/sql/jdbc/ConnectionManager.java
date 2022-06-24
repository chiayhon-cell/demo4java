package cn.chiayhon.jdk.sql.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
public class ConnectionManager implements AutoCloseable {

    private static final ConnectionManager ins = new ConnectionManager();

    private static final ThreadLocal<Connection> connectionMap = new ThreadLocal<>();

    private ConnectionManager() {
    }

    /**
     * get an JDBCManager object
     *
     * @return
     */
    public static ConnectionManager init(String url, String username, String password) {
        ins.setConnection(url, username, password);
        return ins;
    }


    /**
     * get a database for current thread.please
     *
     * @return
     */
    public Connection getConnection() {
        Connection connection = connectionMap.get();
        if (Objects.isNull(connection)) {
            throw new ConnectionNotInitException("the connection has not been initialized");
        }
        return connection;
    }


    public void setConnection(String url, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            // the connection of the current thread can be reused to reduce overhead
            connectionMap.set(connection);
        } catch (SQLException e) {
            log.error("error when getting database connection", e);
        }
    }

    /**
     * close the connection creating by the current thread
     */
    @Override
    public void close() {
        final Connection connection = connectionMap.get();
        if (connection != null) {
            connectionMap.remove();
        }
    }

    static class ConnectionNotInitException extends RuntimeException {
        public ConnectionNotInitException() {
        }

        public ConnectionNotInitException(String message) {
            super(message);
        }

        public ConnectionNotInitException(String message, Throwable cause) {
            super(message, cause);
        }

        public ConnectionNotInitException(Throwable cause) {
            super(cause);
        }

        public ConnectionNotInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
