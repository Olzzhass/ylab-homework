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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Сервисный класс для управления операциями, связанными с пользователями.
 */
public class UserService {

    private static final UserDao userDao = new UserDaoImpl();
    private static final BookingDao bookingDao = new BookingDaoImpl();
    private static final WorkspaceDao workspaceDao = new WorkspaceDaoImpl();

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param registrationUsername имя пользователя для регистрации
     * @param registrationPassword пароль пользователя для регистрации
     * @return true, если пользователь успешно зарегистрирован, false если пользователь с таким именем уже существует
     */
    public boolean registerUser(String registrationUsername, String registrationPassword) {

        Optional<User> maybeUser = userDao.findByUsername(registrationUsername);
        if(maybeUser.isPresent()){
            return false;
        }else{
            userDao.save(new User(registrationUsername, registrationPassword));
            return true;
        }

    }

    /**
     * Метод для аутентификации пользователя.
     *
     * @param authUsername имя пользователя для аутентификации
     * @param authPassword пароль пользователя для аутентификации
     * @return true, если пользователь успешно аутентифицирован, иначе false
     */
    public boolean authenticateUser(String authUsername, String authPassword) {

        // Можно было бы хранить в другом месте
        if(authUsername.equalsIgnoreCase("admin") && authPassword.equalsIgnoreCase("admin123")){
            return true;
        }

        Optional<User> maybeUser = userDao.findByUsername(authUsername);

        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            return user.getPassword().equals(authPassword);
        }

        return false;

    }

    /**
     * Метод для получения всех бронирований пользователя.
     *
     * @param whoLogged имя пользователя, для которого нужно получить бронирования
     * @return список бронирований пользователя
     */
    public List<Booking> showAllReservations(String whoLogged) {

        Optional<User> user = userDao.findByUsername(whoLogged);


        List<Booking> bookings = bookingDao.getByUserId(user.get().getId());

        return bookings;

    }

    /**
     * Метод для получения информации о помещении по его идентификатору.
     *
     * @param id идентификатор помещения
     * @return объект Optional с информацией о помещении, если такое найдено, иначе пустой Optional
     */
    public Optional<Workspace> getWorkspacesById(Long id){
        return workspaceDao.findById(id);
    }

    /**
     * Метод для получения пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект пользователя, если найден, иначе null
     */
    public User getUserById(Long userId){
        return userDao.getById(userId);
    }

}
