package cn.chiayhon.util.database;

import cn.chiayhon.enums.DatabaseType;

import java.io.InputStream;
import java.sql.Connection;

public interface DatabaseInitProcessor {

    /**
     * 执行数据库初始化，默认不忽略执行异常
     *
     * @param connection  数据库连接
     * @param inputStream 数据库文件流
     * @return 初始化结果
     */
    boolean init(Connection connection, InputStream inputStream);

    /**
     * 执行数据库初始化
     *
     * @param connection  数据库连接
     * @param inputStream 数据库文件流
     * @param ignoreError 是否忽略执行异常
     * @return
     */
    boolean init(Connection connection, InputStream inputStream, boolean ignoreError);

    /**
     * 检测该处理器是否支持传入类型{@param databaseType}的初始化操作
     *
     * @param databaseType 数据库类型
     * @return 是否支持
     */
    boolean isSupport(DatabaseType databaseType);
}
