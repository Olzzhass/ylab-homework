package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.exception.NotValidArgumentException;
import kaz.olzhas.ylab.repository.BookingRepository;
import kaz.olzhas.ylab.repository.UserRepository;
import kaz.olzhas.ylab.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для управления операциями, связанными с рабочими местами.
 */

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    /**
     * Метод для отображения всех доступных рабочих мест.
     */
    public void showAllAvailableWorkspaces() {

        List<Workspace> workspaces = workspaceRepository.findAll();
        System.out.println("Доступные рабочие места:");
        for(Workspace workspace : workspaces){
            System.out.println(workspace.getId() + " : " + workspace.getName());
        }

    }

    /**
     * Метод для бронирования рабочего места.
     *
     * @param workspaceId идентификатор рабочего места для бронирования
     * @param start       начальная дата и время бронирования
     * @param end         конечная дата и время бронирования
     * @param whoLogged   имя пользователя, который осуществляет бронирование
     * @return true, если бронирование успешно добавлено, иначе false
     */
    public boolean bookWorkspace(Long workspaceId, LocalDateTime start, LocalDateTime end, String whoLogged) {

        try {
            Optional<User> maybeUser = userRepository.findByUsername(whoLogged);

            if (maybeUser.isEmpty()) {
                throw new NotValidArgumentException("User not found: " + whoLogged);
            }

            User user = maybeUser.get();

            boolean isSaved = bookingRepository.save(user.getId(), workspaceId, start, end);
            if (!isSaved) {
                throw new RuntimeException("Failed to book workspace: " + workspaceId);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error booking workspace: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Метод для удаления бронирования по его номеру.
     *
     * @param bookingNumber номер бронирования для удаления
     * @return true, если бронирование успешно удалено, иначе false
     */
    public boolean deleteReservation(Long bookingNumber) {

        return bookingRepository.deleteById(bookingNumber);

    }

    /**
     * Метод для получения доступных временных слотов для рабочего места на определенную дату.
     *
     * @param workspaceId идентификатор рабочего места
     * @param date        дата, для которой нужно найти доступные временные слоты
     * @return список доступных временных слотов на указанную дату
     */
    public List<LocalDateTime> getAvailableSlots(Long workspaceId, LocalDateTime date){

        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);

        List<Booking> bookings = bookingRepository.getBookingsForWorkspace(workspaceId, startOfDay, endOfDay);

        for (LocalDateTime slot = startOfDay; slot.isBefore(endOfDay); slot = slot.plusHours(1)) {
            boolean isSlotAvailable = true;
            for (Booking booking : bookings) {
                if (booking.overlaps(slot, slot.plusHours(1))) {
                    isSlotAvailable = false;
                    break;
                }
            }
            if (isSlotAvailable) {
                availableSlots.add(slot);
            }
        }

        return availableSlots;

    }
}
