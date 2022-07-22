package cn.chiayhon.util.database;

import cn.chiayhon.enumerate.DatabaseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MySQLDatabaseInitProcessor extends AbstractInitProcessor {

    @Override
    public boolean isSupport(DatabaseType databaseType) {
        return DatabaseType.MYSQL.equals(databaseType);
    }
}
