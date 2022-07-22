package cn.chiayhon.util.database;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Slf4j
public class SQLImportDemo {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/test-temp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        // 获取连接
        final Connection connection = getMysqlConnection(url, username, password);
        // 获取sql测试文件资源
        final URL resource = SQLImportDemo.class.getClassLoader().getResource("temp/test.sql");
        // 获取文件路径
        final String path = Objects.requireNonNull(resource, "未找到测试文件test.sql").getPath();
        // 获取文件中的sql命令
        final String mysqlCommand = getMysqlCommand(path);
        // 执行命令
        execute(connection, mysqlCommand);
    }

    public static void execute(Connection connection, String command) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(command);
        } catch (SQLException throwable) {
            throw new RuntimeException("执行sql失败");
        }
    }

    public static String getMysqlCommand(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String lineStr;
            while (null != (lineStr = reader.readLine())) {
                sb.append(lineStr);
                sb.append(System.lineSeparator());
            }
            final String command = sb.toString();
            log.info("读取sql脚本成功:");
            System.out.println(command);
            return command;

        } catch (IOException e) {
            throw new RuntimeException("读取sql文件异常");
        }
    }

    public static Connection getMysqlConnection(String url, String username, String password) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("获取数据库连接成功");
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("获取连接异常");
        }

    }

}
