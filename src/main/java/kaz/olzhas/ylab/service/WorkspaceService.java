package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.dao.BookingDao;
import kaz.olzhas.ylab.dao.UserDao;
import kaz.olzhas.ylab.dao.WorkspaceDao;
import kaz.olzhas.ylab.dao.implementations.BookingDaoImpl;
import kaz.olzhas.ylab.dao.implementations.UserDaoImpl;
import kaz.olzhas.ylab.dao.implementations.WorkspaceDaoImpl;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для управления операциями, связанными с рабочими местами.
 */
public class WorkspaceService {

    private UserService userService;

    private static final WorkspaceDao workspaceDao = new WorkspaceDaoImpl();
    private static final BookingDao bookingDao = new BookingDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();

    /**
     * Конструктор для инициализации сервиса рабочего места.
     *
     * @param userService сервис пользователей
     */
    public WorkspaceService(UserService userService){
        this.userService = userService;
    }

    /**
     * Метод для отображения всех доступных рабочих мест.
     */
    public void showAllAvailableWorkspaces() {

        List<Workspace> workspaces = workspaceDao.findAll();
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

        Optional<User> maybeUser = userDao.findByUsername(whoLogged);

        User user = maybeUser.get();

        return bookingDao.save(user.getId(), workspaceId, start, end);

    }

    /**
     * Метод для удаления бронирования по его номеру.
     *
     * @param bookingNumber номер бронирования для удаления
     * @return true, если бронирование успешно удалено, иначе false
     */
    public boolean deleteReservation(Long bookingNumber) {

        return bookingDao.deleteById(bookingNumber);

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

        List<Booking> bookings = bookingDao.getBookingsForWorkspace(workspaceId, startOfDay, endOfDay);

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
