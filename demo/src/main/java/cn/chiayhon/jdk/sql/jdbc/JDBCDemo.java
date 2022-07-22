package cn.chiayhon.jdk.sql.jdbc;

import cn.chiayhon.enumerate.Gender;
import cn.chiayhon.pojo.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class JDBCDemo {

    public static void main(String[] args) {
        demo4();
    }

    /**
     * the elementary usage demo of jdbc
     */
    @SuppressWarnings("unused")
    public static void demo1() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // load driver
            // the code of loading driver can be omitted after jdk 6.
            // because it has a more detailed processing method
            // 'getConnection(String url, String user, String password)' in the DriverManager class
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establish a connection by the given Database URL
            String url = "jdbc:mysql://localhost:3306/demo4java?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
            String username = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, username, password);
            // create a Statement object for sending SQL statements to the database
            statement = connection.createStatement();
            // send a query SQL
            resultSet = statement.executeQuery("SELECT * FROM user");
            // get data
            while (resultSet.next()) {
                final User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setAge(resultSet.getInt("age"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                log.info("the result of query：" + user);
            }
        } catch (Exception e) {
            log.error("error when executing", e);
        } finally {
            // close resource
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("fail closing resource", e);
            }
        }
    }

    /**
     * the elementary usage demo by {@link  ConnectionManager} of jdbc
     */
    public static void demo2() {
        String url = "jdbc:mysql://localhost:3306/demo4java?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        PreparedStatement preparedStatement = null;
        try (ConnectionManager manager = ConnectionManager.init(url, username, password)) {
            Connection connection = manager.getConnection();
            String sql = "SELECT * FROM user";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            // get data
            while (resultSet.next()) {
                final User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setAge(resultSet.getInt("age"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                log.info("the result of query：" + user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error("fail closing statement");
                }
            }
        }
    }

    /**
     * the update usage demo of jdbc
     */
    public static void demo3() {
        String url = "jdbc:mysql://localhost:3306/demo4java?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        PreparedStatement preparedStatement = null;
        try (ConnectionManager manager = ConnectionManager.init(url, username, password)) {
            Connection connection = manager.getConnection();
            String sql = "UPDATE user u SET u.username = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            // fill parameters
            preparedStatement.setString(1, "张三");
            preparedStatement.setLong(2, 1);
            //
            final int effectRowNum = preparedStatement.executeUpdate();
            // get data
            if (effectRowNum == 0) {
                log.error("update fail, nothing changed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error("fail closing statement");
                }
            }
        }
    }

    /**
     * the transaction demo of jdbc
     */
    public static void demo4() {
        String url = "jdbc:mysql://localhost:3306/demo4java?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        Connection connection = null;
        try (ConnectionManager manager = ConnectionManager.init(url, username, password)) {
            connection = manager.getConnection();
            // disable automatic committing
            connection.setAutoCommit(false);
            // do update
            update(connection);
            throw new RuntimeException("runtime exception");
        } catch (Exception e) {
            // catch exception,the transaction should roll back
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("roll back failed");
            }
            log.error("update failed", e);
        } finally {
            try {
                // commit transaction
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("commit failed", e);
            }
        }

    }

    private static void update(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "UPDATE user u SET u.username = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            // fill parameters
            preparedStatement.setString(1, "张三1");
            preparedStatement.setLong(2, 1);
            //
            final int effectRowNum = preparedStatement.executeUpdate();
            // get data
            if (effectRowNum == 0) {
                log.error("update fail, nothing changed");
            }
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error("fail closing statement");
                }
            }
        }
    }
}
