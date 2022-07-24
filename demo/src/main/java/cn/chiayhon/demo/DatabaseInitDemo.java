package cn.chiayhon.demo;

import cn.chiayhon.enumerate.DatabaseType;
import cn.chiayhon.exception.BizException;
import cn.chiayhon.properties.DatabaseProperties;
import cn.chiayhon.rest.service.DatabaseManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Slf4j
@Component
public class DatabaseInitDemo {

    @Autowired
    DatabaseManageService databaseManageService;

    public void main() {
        DatabaseProperties databaseProperties = new DatabaseProperties(
                DatabaseType.MYSQL, // 数据库类型
                "test-demo", // 数据库名
                "localhost", //数据库地址
                3306, // 数据库端口
                "root", //数据库账号
                "chiayhon" // 数据库密码
        );
        final InputStream inputStream = DatabaseInitDemo.class.getClassLoader().getResourceAsStream("temp/test.sql");
        final boolean success = databaseManageService.initDatabase(databaseProperties, inputStream);
        log.info("初始化的结果:" + success);
    }

    // demo1
    public void demo1() {
        String url = "jdbc:mysql://127.0.0.1:3306/test-temp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        // 获取连接
        final Connection connection = getMysqlConnection(url, username, password);
        // 获取sql测试文件资源
        final URL resource = DatabaseInitDemo.class.getClassLoader().getResource("temp/test.sql");
        // 获取文件路径
        final String path = Objects.requireNonNull(resource, "未找到测试文件test.sql").getPath();
        // 获取文件中的sql命令
        final String mysqlCommand = getMysqlCommand(path);
        // 执行命令
        execute(connection, mysqlCommand);
    }

    public void execute(Connection connection, String command) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(command);
        } catch (SQLException e) {
            throw new BizException("执行sql失败", e);
        }
    }

    public String getMysqlCommand(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String lineStr;
            while (null != (lineStr = reader.readLine())) {
                sb.append(lineStr);
                sb.append(System.lineSeparator());
            }
            final String command = sb.toString();
            log.info("读取sql脚本成功:\n{}", command);
            return command;

        } catch (IOException e) {
            throw new BizException("读取sql文件异常", e);
        }
    }

    public Connection getMysqlConnection(String url, String username, String password) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("获取数据库连接成功");
            return connection;
        } catch (SQLException e) {
            throw new BizException("获取连接异常", e);
        }
    }


}
