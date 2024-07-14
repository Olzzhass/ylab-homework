package kaz.olzhas.ylab.repository.implementations;

import kaz.olzhas.ylab.entity.Audit;
import kaz.olzhas.ylab.entity.types.ActionType;
import kaz.olzhas.ylab.repository.AuditRepository;
import kaz.olzhas.ylab.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с аудитом действий в системе.
 * Использует JDBC для взаимодействия с базой данных PostgreSQL.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    private final ConnectionManager connectionManager;

    /**
     * Находит аудит по его идентификатору.
     *
     * @param id идентификатор аудита.
     * @return Optional сущности Audit, если аудит найден, или Optional.empty(), если не найден.
     */
    @Override
    public Optional<Audit> findById(Long id) {
        String query = """
                SELECT * FROM log.auditLog WHERE id = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(getAudit(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            log.error("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Возвращает список всех записей аудита.
     *
     * @return список аудитов.
     */
    @Override
    public List<Audit> findAll() {
        String query = """
                SELECT * FROM log.auditLog;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Audit> audits = new ArrayList<>();

            while (resultSet.next()) {
                audits.add(getAudit(resultSet));
            }

            return audits;
        } catch (SQLException e) {
            log.error("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Сохраняет запись аудита в базу данных.
     *
     * @param audit объект аудита для сохранения.
     * @return true, если аудит успешно сохранен, иначе false.
     */
    @Override
    public boolean save(Audit audit) {
        String query = """
                INSERT INTO log."auditLog"(username, action, timestamp)
                VALUES (?,?,?);
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, audit.getUsername());
            preparedStatement.setObject(2, audit.getActionType(), Types.OTHER);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        audit.setId(keys.getLong(1));
                    }
                } catch (SQLException e) {
                    log.error("Ошибка при получении сгенерированного ключа: " + e.getMessage());
                }
            } else {
                log.error("Ошибка при выполнении SQL-запроса. Нет добавленных записей.");
            }

            return true;
        } catch (SQLException e) {
            log.error("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Создает объект Audit на основе данных из ResultSet.
     *
     * @param resultSet ResultSet с данными аудита из базы данных.
     * @return объект Audit.
     * @throws SQLException если произошла ошибка при работе с ResultSet.
     */
    public Audit getAudit(ResultSet resultSet) throws SQLException {
        Audit audit = new Audit();

        audit.setId(resultSet.getLong("id"));
        audit.setUsername(resultSet.getString("username"));

        String actionTypeString = resultSet.getString("action");
        ActionType actionType = ActionType.valueOf(actionTypeString);

        audit.setActionType(actionType);
        audit.setAuditTime(resultSet.getTimestamp("timestamp").toLocalDateTime());

        return audit;
    }
}
