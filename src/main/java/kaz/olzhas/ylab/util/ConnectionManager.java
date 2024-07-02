package kaz.olzhas.ylab.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Класс ConnectionManager управляет подключением к базе данных.
 * Этот класс предоставляет методы для получения соединения с базой данных и загрузки драйвера базы данных.
 */
public class ConnectionManager {
    private final String URL;
    private final String username;
    private final String password;

    /**
     * Конструктор ConnectionManager.
     *
     * @param URL      URL базы данных
     * @param username имя пользователя базы данных
     * @param password пароль пользователя базы данных
     * @param driver   драйвер базы данных
     */
    public ConnectionManager(String URL, String username, String password, String driver) {
        this.URL = URL;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для получения соединения с базой данных.
     *
     * @return объект Connection для подключения к базе данных
     * @throws RuntimeException если возникла ошибка при подключении
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
