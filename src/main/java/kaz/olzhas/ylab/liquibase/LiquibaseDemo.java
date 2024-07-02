package kaz.olzhas.ylab.liquibase;

import kaz.olzhas.ylab.util.ConnectionManager;
import kaz.olzhas.ylab.util.PropertiesUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Класс для выполнения миграций базы данных с использованием Liquibase.
 */

@AllArgsConstructor
public class LiquibaseDemo {

    /**
     * A singleton instance of the `LiquibaseDemo` class.
     */
    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS liquibase";

    private final Connection connection;
    private final String changeLogFile;
    private final String schemaName;

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(schemaName);

            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}