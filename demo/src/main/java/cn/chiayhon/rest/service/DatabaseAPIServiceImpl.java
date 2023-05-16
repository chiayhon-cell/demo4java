package cn.chiayhon.rest.service;

import cn.chiayhon.enums.DatabaseType;
import cn.chiayhon.properties.DatabaseProperties;
import cn.chiayhon.util.database.ConnectionUtil;
import cn.chiayhon.util.database.DatabaseInitProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

@Service
public class DatabaseAPIServiceImpl implements DatabaseAPIService {

    @Autowired
    private List<DatabaseInitProcessor> processorList;

    @Override
    public Connection getConnection(DatabaseProperties databaseProperties) {
        return ConnectionUtil.getManager().getConnection(databaseProperties);
    }

    @Override
    public DatabaseInitProcessor getInitProcessor(DatabaseType databaseType) {
        for (DatabaseInitProcessor processor : processorList) {
            if (processor.isSupport(databaseType)) {
                return processor;
            }
        }
        return null;
    }

    @Override
    public DatabaseInitProcessor getInitProcessor(Class<DatabaseInitProcessor> aClass) {
        for (DatabaseInitProcessor processor : processorList) {
            if (processor.getClass().equals(aClass)) {
                return processor;
            }
        }
        return null;
    }

}
