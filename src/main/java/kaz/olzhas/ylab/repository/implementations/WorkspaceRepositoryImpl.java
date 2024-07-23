package kaz.olzhas.ylab.repository.implementations;

import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.repository.WorkspaceRepository;
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
 * Реализация репозитория для работы с помещениями (workspace) в базе данных.
 * Использует JDBC для взаимодействия с PostgreSQL.
 */
@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final ConnectionManager connectionManager;

    /**
     * Возвращает список всех помещений из базы данных.
     *
     * @return список всех помещений.
     */
    @Override
    public List<Workspace> findAll() {
        String query = """
                SELECT * FROM tables.workspace
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Workspace> workspaceList = new ArrayList<>();

            while (resultSet.next()) {
                workspaceList.add(getWorkspace(resultSet));
            }

            return workspaceList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ищет помещение по его идентификатору.
     *
     * @param id идентификатор помещения для поиска.
     * @return Optional с найденным помещением, если оно существует, иначе пустой Optional.
     */
    @Override
    public Optional<Workspace> findById(Long id) {
        String query = """
                SELECT * FROM tables.workspace WHERE id = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(getWorkspace(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохраняет новое помещение в базе данных.
     *
     * @param workspace объект помещения для сохранения.
     * @return true, если помещение успешно сохранено, иначе false.
     */
    @Override
    public boolean save(Workspace workspace) {
        String query = """
                INSERT INTO tables.workspace(name) VALUES (?)
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, workspace.getName());
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Создает объект Workspace на основе данных из ResultSet.
     *
     * @param resultSet ResultSet с данными помещения из базы данных.
     * @return объект Workspace.
     * @throws SQLException если произошла ошибка при работе с ResultSet.
     */
    private Workspace getWorkspace(ResultSet resultSet) throws SQLException {
        Workspace workspace = new Workspace();

        workspace.setId(resultSet.getLong("id"));
        workspace.setName(resultSet.getString("name"));

        return workspace;
    }
}
