package cn.chiayhon.rest.service;

import cn.chiayhon.properties.DatabaseProperties;
import cn.chiayhon.util.database.DatabaseInitProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Connection;

@Service
public class DatabaseManageServiceImpl implements DatabaseManageService {

    @Autowired
    private DatabaseAPIService databaseAPIService;

    public boolean initDatabase(DatabaseProperties databaseProperties, InputStream inputStream) {
        // 获取数据库连接
        final Connection connection = databaseAPIService.getConnection(databaseProperties);

        // 获取对应数据库的初始化执行器
        final DatabaseInitProcessor initProcessor = databaseAPIService.getInitProcessor(databaseProperties.getDatabase());

        // 执行器进行初始化
        return initProcessor.init(connection, inputStream);
    }
}
