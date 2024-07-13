package kaz.olzhas.ylab.repository.implementations;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.repository.BookingRepository;
import kaz.olzhas.ylab.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для работы с бронированием рабочих мест в базе данных.
 * Использует JDBC для взаимодействия с PostgreSQL.
 */
@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final ConnectionManager connectionManager;

    /**
     * Сохраняет бронь в базе данных.
     *
     * @param userId      идентификатор пользователя.
     * @param workspaceId идентификатор рабочего места.
     * @param start       время начала брони.
     * @param end         время окончания брони.
     * @return true, если бронь успешно сохранена, иначе false.
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
     * Возвращает список бронирований пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return список объектов Booking, соответствующих пользователям.
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
     * Удаляет бронирование по его идентификатору.
     *
     * @param id идентификатор бронирования.
     * @return true, если бронирование успешно удалено, иначе false.
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
     * Возвращает список бронирований по идентификатору рабочего места.
     *
     * @param workspaceId идентификатор рабочего места.
     * @return список бронирований для указанного рабочего места.
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
     * Возвращает список бронирований для указанного рабочего места в заданный период времени.
     *
     * @param workspaceId идентификатор рабочего места.
     * @param start       время начала периода.
     * @param end         время окончания периода.
     * @return список бронирований для указанного рабочего места в заданный период времени.
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
     * Удаляет все бронирования из таблицы.
     *
     * @return true, если бронирования успешно удалены, иначе false.
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
     * Создает объект Booking на основе данных из ResultSet.
     *
     * @param resultSet ResultSet с данными бронирования из базы данных.
     * @return объект Booking.
     * @throws SQLException если произошла ошибка при работе с ResultSet.
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
