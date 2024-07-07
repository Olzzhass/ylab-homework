package kaz.olzhas.ylab.dao.implementations;

import kaz.olzhas.ylab.dao.BookingDao;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.util.ConnectionManager;

import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDaoImpl implements BookingDao {

    private final ConnectionManager connectionManager;

    public BookingDaoImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    /**
     * Метод для сохранения брони
     * @param userId
     * @param workspaceId
     * @param start
     * @param end
     * @return
     */

    @Override
    public boolean save(Long userId, Long workspaceId, LocalDateTime start, LocalDateTime end) {

        String query = """
                INSERT INTO tables.booking(user_id, workspace_id, start_time, end_time) VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, workspaceId);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(end));

            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * получения броней по пользователю
     * @param userId
     * @return
     */
    @Override
    public List<Booking> getByUserId(Long userId) {

        String query = """
                SELECT * FROM tables.booking WHERE user_id = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()){
                bookings.add(getBooking(resultSet));
            }

            return bookings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаление брони по id номеру
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Long id) {

        String query = """
                DELETE FROM tables.booking WHERE id = ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение всех броней определенного рабочего места
     * @param workspaceId
     * @return
     */
    @Override
    public List<Booking> getByWorkspaceId(Long workspaceId) {

        String query = """
                SELECT * FROM tables.booking WHERE workspace_id = ?
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, workspaceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()){
                bookings.add(getBooking(resultSet));
            }

            return bookings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение броней по рабочему месту с учетом определнного времени
     * @param workspaceId
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<Booking> getBookingsForWorkspace(Long workspaceId, LocalDateTime start, LocalDateTime end) {

        String query = """
                SELECT * FROM tables.booking WHERE workspace_id = ? AND start_time >= ? AND end_time <= ?
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, workspaceId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(end));

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()){
                bookings.add(getBooking(resultSet));
            }

            return bookings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для удаления(очистки таблицы)
     * @return
     */
    @Override
    public boolean deleteAll() {
        String query = """
                TRUNCATE TABLE tables.booking;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            return preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Переоброзавать ResultSet в объект класса Booking
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Booking getBooking(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();

        booking.setId(resultSet.getLong("id"));
        booking.setUserId(resultSet.getLong("user_id"));
        booking.setWorkspaceId(resultSet.getLong("workspace_id"));
        booking.setStart(resultSet.getTimestamp("start_time").toLocalDateTime());
        booking.setEnd(resultSet.getTimestamp("end_time").toLocalDateTime());

        return booking;
    }
}
