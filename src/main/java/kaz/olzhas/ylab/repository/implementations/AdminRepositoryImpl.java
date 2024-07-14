package kaz.olzhas.ylab.repository.implementations;

import kaz.olzhas.ylab.entity.Admin;
import kaz.olzhas.ylab.repository.AdminRepository;
import kaz.olzhas.ylab.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Реализация репозитория для работы с данными администраторов в базе данных.
 * Использует JDBC для взаимодействия с базой данных PostgreSQL.
 */
@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final ConnectionManager connectionManager;

    /**
     * Находит администратора по его имени пользователя.
     *
     * @param username имя пользователя администратора.
     * @return Optional сущности Admin, если администратор найден, или Optional.empty(), если не найден.
     * @throws RuntimeException если произошла ошибка SQL или JDBC.
     */
    @Override
    public Optional<Admin> findByUsername(String username) {
        String query = """
                SELECT * FROM tables.admin WHERE "adminName" = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(getAdmin(resultSet)) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Admin getAdmin(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();

        admin.setId(resultSet.getLong("id"));
        admin.setAdminName(resultSet.getString("adminName"));
        admin.setAdminPassword(resultSet.getString("adminPassword"));

        return admin;
    }
}
