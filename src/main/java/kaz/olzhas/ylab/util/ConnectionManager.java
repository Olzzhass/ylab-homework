package kaz.olzhas.ylab.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionManager {
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;

    static {
        loadDriver();
        initConnectionPool();
    }

    /**
     * Загрузка драйвера PostgreSQL.
     */
    private static void loadDriver(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ConnectionManager(){}

    /**
     * Инициализация пула соединений.
     */
    private static void initConnectionPool() {
        int size = DEFAULT_POOL_SIZE;
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>();
        for(int i = 0; i < size; i++){
            var connection = open();

            var proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? pool.add((Connection) proxy)
                                    : method.invoke(connection, args));

            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    /**
     * Получение соединения из пула.
     *
     * @return Соединение с базой данных PostgreSQL.
     */
    public static Connection get(){
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Открытие нового соединения с базой данных PostgreSQL.
     *
     * @return Новое соединение с базой данных PostgreSQL.
     */
    private static Connection open(){
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Закрытие всех исходных соединений пула.
     */
    public static void closePool() {
        try {
            for (Connection sourceConnection : sourceConnections) {
                sourceConnection.close();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
