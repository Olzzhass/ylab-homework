package kaz.olzhas.ylab.dao.implementations;

import kaz.olzhas.ylab.dao.AdminDao;
import kaz.olzhas.ylab.entity.Admin;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDaoImpl implements AdminDao {
    private final ConnectionManager connectionManager;

    public AdminDaoImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }
    @Override
    public Optional<Admin> findByUsername(String username) {
        String query = """
                SELECT * FROM tables.admin WHERE "adminName" = ?
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

    private Admin getUser(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();

        admin.setId(resultSet.getLong("id"));
        admin.setAdminName(resultSet.getString("adminName"));
        admin.setAdminPassword(resultSet.getString("adminPassword"));

        return admin;
    }
}
