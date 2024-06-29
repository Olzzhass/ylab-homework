package kaz.olzhas.ylab.dao.implementations;

import kaz.olzhas.ylab.dao.UserDao;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса UserDao для взаимодействия с таблицей "user" в базе данных.
 */
public class UserDaoImpl implements UserDao {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска.
     * @return Optional, содержащий найденного пользователя, или пустой Optional, если пользователь не найден.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        String query = """
                SELECT * FROM tables."user" WHERE username = ?
                """;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(getUser(resultSet)) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Извлекает всех пользователей из таблицы "user".
     *
     * @return список всех пользователей.
     */
    @Override
    public List<User> findAll() {
        String query = """
                SELECT * FROM tables."user"
                """;

        try (var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(query)) {

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
     * Сохраняет пользователя в таблице "user".
     *
     * @param user пользователь для сохранения.
     * @return true, если пользователь был успешно сохранен, иначе false.
     */
    @Override
    public boolean save(User user) {
        String query = """
                INSERT INTO tables."user"(username, password) VALUES (?, ?)
                """;

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Извлекает пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя для поиска.
     * @return пользователь с указанным идентификатором или null, если пользователь не найден.
     */
    @Override
    public User getById(Long userId) {

        String query = """
                SELECT * FROM tables."user" WHERE id = ?
                """;

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? getUser(resultSet) : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Преобразует строку результата в объект User.
     *
     * @param resultSet строка результата из базы данных.
     * @return объект User.
     * @throws SQLException если возникает ошибка при извлечении данных из resultSet.
     */
    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));

        return user;
    }
}
