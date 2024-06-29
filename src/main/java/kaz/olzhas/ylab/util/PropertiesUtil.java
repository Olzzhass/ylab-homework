package kaz.olzhas.ylab.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    // Статический блок инициализации для загрузки свойств из файла
    static {
        loadProperties();
    }

    private PropertiesUtil(){}; // Приватный конструктор для предотвращения создания экземпляров

    /**
     * Получает значение свойства по заданному ключу.
     *
     * @param key Ключ свойства.
     * @return Значение свойства в виде строки.
     */
    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    /**
     * Загружает свойства из файла "application.properties",
     * расположенного в classpath приложения.
     */
    private static void loadProperties(){
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
