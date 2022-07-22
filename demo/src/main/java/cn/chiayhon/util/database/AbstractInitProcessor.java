package cn.chiayhon.util.database;

import cn.chiayhon.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public abstract class AbstractInitProcessor implements DatabaseInitProcessor {

    @Override
    public boolean init(Connection connection, InputStream inputStream) {
        return init(connection, inputStream, false);
    }

    @Override
    public boolean init(Connection connection, InputStream inputStream, boolean ignoreError) {
        String command = getCommand(inputStream);
        try (Statement statement = connection.createStatement()) {
            log.info("开始执行sql脚本:\n{}", command);
            statement.execute(command);
            log.info("执行脚本成功");
            return true;
        } catch (SQLException e) {
            if (!ignoreError) {
                throw new BizException("执行sql失败", e);
            } else {
                log.error("执行sql失败", e);
                return false;
            }
        }
    }

    protected String getCommand(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String lineStr;
            while (null != (lineStr = reader.readLine())) {
                sb.append(lineStr);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new BizException("解析SQL文件流时异常", e);
        }
    }

}
