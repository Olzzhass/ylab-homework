package kaz.olzhas.ylab.dao.implementations;

import kaz.olzhas.ylab.dao.AuditLogDao;
import kaz.olzhas.ylab.entity.AuditLog;
import kaz.olzhas.ylab.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogDaoImpl implements AuditLogDao {

    private final ConnectionManager connectionManager;

    public AuditLogDaoImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    @Override
    public void save(AuditLog auditLog) {
        String query = """
                INSERT INTO log.auditLog(username, action, timestamp) VALUES (?, ?, ?);
                """;

        try (Connection connection = connectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, auditLog.getUsername());
            preparedStatement.setString(2, auditLog.getAction());
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(auditLog.getTimestamp()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
