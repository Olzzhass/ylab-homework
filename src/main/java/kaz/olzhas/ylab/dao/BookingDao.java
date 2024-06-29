package kaz.olzhas.ylab.dao;

import kaz.olzhas.ylab.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс для взаимодействия с бронированиями в базе данных.
 */
public interface BookingDao {

    /**
     * Сохраняет бронирование в базе данных.
     *
     * @param userId идентификатор пользователя.
     * @param workspaceId идентификатор рабочего пространства.
     * @param start время начала бронирования.
     * @param end время окончания бронирования.
     * @return true, если бронирование было успешно сохранено, иначе false.
     */
    boolean save(Long userId, Long workspaceId, LocalDateTime start, LocalDateTime end);

    /**
     * Извлекает все бронирования для указанного пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список бронирований пользователя.
     */
    List<Booking> getByUserId(Long userId);

    /**
     * Удаляет бронирование по его идентификатору.
     *
     * @param id идентификатор бронирования.
     * @return true, если бронирование было успешно удалено, иначе false.
     */
    boolean deleteById(Long id);

    /**
     * Извлекает все бронирования для указанного рабочего пространства.
     *
     * @param workspaceId идентификатор рабочего пространства.
     * @return список бронирований рабочего пространства.
     */
    List<Booking> getByWorkspaceId(Long workspaceId);

    /**
     * Извлекает бронирования для указанного рабочего пространства в заданный период.
     *
     * @param workspaceId идентификатор рабочего пространства.
     * @param start время начала периода.
     * @param end время окончания периода.
     * @return список бронирований рабочего пространства в указанный период.
     */
    List<Booking> getBookingsForWorkspace(Long workspaceId, LocalDateTime start, LocalDateTime end);
}
