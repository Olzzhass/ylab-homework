package kaz.olzhas.ylab.repository;

import kaz.olzhas.ylab.entity.Booking;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с бронированиями помещений.
 */
public interface BookingRepository {

    /**
     * Сохраняет новое бронирование помещения.
     *
     * @param userId      идентификатор пользователя, который бронирует помещение.
     * @param workspaceId идентификатор помещения, которое бронируется.
     * @param start       начальное время бронирования.
     * @param end         конечное время бронирования.
     * @return true, если бронирование сохранено успешно, false в противном случае.
     */
    boolean save(Long userId, Long workspaceId, LocalDateTime start, LocalDateTime end);

    /**
     * Возвращает список всех бронирований пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список бронирований пользователя.
     */
    List<Booking> getByUserId(Long userId);

    /**
     * Удаляет бронирование по его идентификатору.
     *
     * @param id идентификатор бронирования для удаления.
     * @return true, если бронирование успешно удалено, false в противном случае.
     */
    boolean deleteById(Long id);

    /**
     * Возвращает список всех бронирований для указанного помещения.
     *
     * @param workspaceId идентификатор помещения.
     * @return список бронирований для указанного помещения.
     */
    List<Booking> getByWorkspaceId(Long workspaceId);

    /**
     * Возвращает список бронирований для указанного помещения в заданном временном интервале.
     *
     * @param workspaceId идентификатор помещения.
     * @param start       начальное время интервала бронирования.
     * @param end         конечное время интервала бронирования.
     * @return список бронирований для указанного помещения в заданном временном интервале.
     */
    List<Booking> getBookingsForWorkspace(Long workspaceId, LocalDateTime start, LocalDateTime end);

    /**
     * Удаляет все бронирования из системы.
     *
     * @return true, если все бронирования успешно удалены, false в противном случае.
     */
    boolean deleteAll();
}
