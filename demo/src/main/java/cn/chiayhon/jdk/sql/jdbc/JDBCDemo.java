package cn.chiayhon.jdk.sql.jdbc;

import cn.chiayhon.pojo.Gender;
import cn.chiayhon.pojo.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class JDBCDemo {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        demo1();
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
                log.error("fail to close resource", e);
            }
        }
    }

    /**
     * the elementary usage demo by {@link  cn.chiayhon.jdk.sql.jdbc.JDBCUtil} of jdbc
     */
    public static void demo2() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/demo4java?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        JDBCUtil.init(driver, url, username, password);
    }
}
