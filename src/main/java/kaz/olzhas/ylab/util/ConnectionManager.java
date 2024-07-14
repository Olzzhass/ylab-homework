package kaz.olzhas.ylab.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс ConnectionManager управляет подключением к базе данных.
 * Этот класс предоставляет методы для получения соединения с базой данных и загрузки драйвера базы данных.
 */
@Component
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class ConnectionManager {

    @Value("${datasource.url}")
    private String URL;
    @Value("${datasource.driver-class-name}")
    private String driver;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;

    /**
     * Получает соединение с базой данных, используя настройки из application.yml.
     *
     * @return объект Connection для взаимодействия с базой данных
     * @throws RuntimeException если возникает ошибка при подключении к базе данных
     */
    public Connection getConnection() {
        try {
            Class.forName(driver);

            return DriverManager.getConnection(
                    URL,
                    username,
                    password
            );
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получает соединение с базой данных с заданными параметрами.
     *
     * @param URL      URL базы данных
     * @param username имя пользователя базы данных
     * @param password пароль пользователя базы данных
     * @param driver   класс драйвера базы данных
     * @return объект Connection для взаимодействия с базой данных
     * @throws RuntimeException если возникает ошибка при подключении к базе данных
     */
    public Connection getConnection(String URL, String username, String password, String driver) {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(URL, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
