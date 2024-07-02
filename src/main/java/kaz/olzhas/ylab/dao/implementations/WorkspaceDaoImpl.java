package kaz.olzhas.ylab.dao.implementations;

import kaz.olzhas.ylab.dao.WorkspaceDao;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса WorkspaceDao для взаимодействия с таблицей "workspace" в базе данных.
 */
public class WorkspaceDaoImpl implements WorkspaceDao {

    private final ConnectionManager connectionManager;

    public WorkspaceDaoImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    /**
     * Извлекает все рабочие пространства из таблицы "workspace".
     *
     * @return список всех рабочих пространств.
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

            while (resultSet.next()){
                workspaceList.add(getWorkspace(resultSet));
            }

            return workspaceList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Находит рабочее пространство по его идентификатору.
     *
     * @param id идентификатор рабочего пространства для поиска.
     * @return Optional, содержащий найденное рабочее пространство, или пустой Optional, если рабочее пространство не найдено.
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
     * Сохраняет рабочее пространство в таблице "workspace".
     *
     * @param workspace рабочее пространство для сохранения.
     * @return true, если рабочее пространство было успешно сохранено, иначе false.
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
     * Преобразует строку результата в объект Workspace.
     *
     * @param resultSet строка результата из базы данных.
     * @return объект Workspace.
     * @throws SQLException если возникает ошибка при извлечении данных из resultSet.
     */
    private Workspace getWorkspace(ResultSet resultSet) throws SQLException {
        Workspace workspace = new Workspace();

        workspace.setId(resultSet.getLong("id"));
        workspace.setName(resultSet.getString("name"));

        return workspace;
    }
}
