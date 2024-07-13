package kaz.olzhas.ylab.repository.implementations;

import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.repository.UserRepository;
import kaz.olzhas.ylab.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с пользователями в базе данных.
 * Использует JDBC для взаимодействия с PostgreSQL.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final ConnectionManager connectionManager;

    /**
     * Ищет пользователя по его имени пользователя (username).
     *
     * @param username имя пользователя для поиска.
     * @return Optional с найденным пользователем, если он существует, иначе пустой Optional.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        String query = """
                SELECT * FROM tables."user" WHERE username = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(getUser(resultSet)) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает список всех пользователей из базы данных.
     *
     * @return список всех пользователей.
     */
    @Override
    public List<User> findAll() {
        String query = """
                SELECT * FROM tables."user"
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()){
                users.add(getUser(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохраняет нового пользователя в базе данных.
     *
     * @param user объект пользователя для сохранения.
     * @return true, если пользователь успешно сохранен, иначе false.
     */
    @Override
    public boolean save(User user) {
        String query = """
                INSERT INTO tables."user"(username, password) VALUES (?, ?)
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return объект User, соответствующий указанному идентификатору, или null, если пользователь не найден.
     */
    @Override
    public User getById(Long userId) {
        String query = """
                SELECT * FROM tables."user" WHERE id = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? getUser(resultSet) : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает объект User на основе данных из ResultSet.
     *
     * @param resultSet ResultSet с данными пользователя из базы данных.
     * @return объект User.
     * @throws SQLException если произошла ошибка при работе с ResultSet.
     */
    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));

        return user;
    }
}
