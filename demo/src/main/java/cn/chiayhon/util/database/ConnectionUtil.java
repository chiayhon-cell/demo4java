package cn.chiayhon.util.database;

import cn.chiayhon.bo.ConnectionManager;
import cn.chiayhon.bo.DefaultConnectionManager;

public class ConnectionUtil {

    private ConnectionUtil() {
    }

    private static final ConnectionManager DEFAULT_MANAGER = new DefaultConnectionManager();

    public static ConnectionManager getManager() {
        return DEFAULT_MANAGER;
    }
}
