package kaz.olzhas.ylab.liquibase;

import kaz.olzhas.ylab.util.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Класс для выполнения миграций базы данных с использованием Liquibase.
 */
public class LiquibaseDemo {

    /**
     * Единственный экземпляр класса `LiquibaseDemo`.
     */
    private static final LiquibaseDemo liquibaseDemo = new LiquibaseDemo();

    /**
     * Метод для запуска миграций базы данных.
     */
    public void runMigrations() {
        try (var connection = ConnectionManager.get()) {

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/main-changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения единственного экземпляра класса `LiquibaseDemo`.
     *
     * @return единственный экземпляр класса `LiquibaseDemo`
     */
    public static LiquibaseDemo getInstance() {
        return liquibaseDemo;
    }
}
