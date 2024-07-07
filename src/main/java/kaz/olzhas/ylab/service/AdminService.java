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
import kaz.olzhas.ylab.util.ConnectionManager;
import kaz.olzhas.ylab.util.PropertiesUtil;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Сервисный класс для администратора, обеспечивающий доступ к операциям над помещениями, пользователями и бронированиями.
 */
public class AdminService {
    private WorkspaceService workspaceService;
    private UserService userService;
    private final WorkspaceDao workspaceDao;
    private final UserDao userDao;
    private final BookingDao bookingDao;

    private ConnectionManager connectionManager;

    /**
     * Конструктор для инициализации сервиса администратора.
     *
     * @param workspaceService сервис для управления помещениями
     * @param userService     сервис для управления пользователями
     */
    public AdminService(WorkspaceService workspaceService, UserService userService, ConnectionManager connectionManager) {
        this.workspaceService = workspaceService;
        this.userService = userService;
        this.connectionManager = connectionManager;
        this.userDao = new UserDaoImpl(connectionManager);
        this.workspaceDao = new WorkspaceDaoImpl(connectionManager);
        this.bookingDao = new BookingDaoImpl(connectionManager);
    }

    /**
     * Метод для добавления нового помещения.
     *
     * @param name имя нового помещения
     */
    public void addWorkspace(String name){

        Workspace workspace = new Workspace();
        workspace.setName(name);

        workspaceDao.save(workspace);
        System.out.println("Рабочее место успешно добавлено.");

    }

    /**
     * Метод для просмотра всех помещений.
     *
     * @return список всех помещений
     */
    public List<Workspace> seeAllWorkspaces(){

        return workspaceDao.findAll();

    }

    /**
     * Метод для просмотра всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> seeAllUsers(){

        return userDao.findAll();

    }

    /**
     * Метод для просмотра всех пользователей без вывода пароля.
     */
    public void seeAllUsersWithoutPassword(){

        List<User> users = userDao.findAll();
        if(users.size() == 0){
            System.out.println("Пока что пользователей нет.");
        }else{
            System.out.println("ВСЕ ПОЛЬЗОВАТЕЛИ:");
            for(User user : users){
                System.out.println("Username: " + user.getUsername());
            }
        }

    }

    /**
     * Метод для просмотра всех бронирований по определенному помещению.
     *
     * @param workspaceId идентификатор помещения
     * @return список бронирований для указанного помещения
     */
    public List<Booking> bookingsByWorkspace(Long workspaceId){

        return bookingDao.getByWorkspaceId(workspaceId);

    }

    /**
     * Метод для просмотра всех бронирований по определенному пользователю.
     *
     * @param username имя пользователя
     */
    public void bookingsByUser(String username){

        Optional<User> maybeUser = userDao.findByUsername(username);

        List<Booking> bookings = bookingDao.getByUserId(maybeUser.get().getId());

        if(bookings.size() == 0){
            System.out.println("Пользователь еще не забронировал себе место.");
        }else{
            for(Booking booking : bookings){

                Optional<Workspace> workspace = workspaceDao.findById(booking.getWorkspaceId());

                System.out.println(workspace.get().getName() + " - " + booking.getStart() + " : " + booking.getEnd());
            }
        }


    }
}
