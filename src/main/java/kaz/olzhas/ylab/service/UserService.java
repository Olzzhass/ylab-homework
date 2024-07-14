package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.annotations.Auditable;
import kaz.olzhas.ylab.annotations.Loggable;
import kaz.olzhas.ylab.entity.Booking;
import kaz.olzhas.ylab.entity.User;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.entity.types.ActionType;
import kaz.olzhas.ylab.exception.NotValidArgumentException;
import kaz.olzhas.ylab.exception.RegisterException;
import kaz.olzhas.ylab.repository.BookingRepository;
import kaz.olzhas.ylab.repository.UserRepository;
import kaz.olzhas.ylab.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для управления операциями, связанными с пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param registrationUsername имя пользователя для регистрации
     * @param registrationPassword пароль пользователя для регистрации
     * @return true, если пользователь успешно зарегистрирован, false если пользователь с таким именем уже существует
     */
    @Loggable
    public boolean registerUser(String registrationUsername, String registrationPassword) {

        if (registrationUsername == null || registrationPassword == null || registrationUsername.isEmpty() || registrationPassword.isEmpty()) {
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
        }

        Optional<User> maybeUser = userRepository.findByUsername(registrationUsername);
        if (maybeUser.isPresent()) {
            throw new RegisterException("Пользователь с таким именем уже существует");
        } else {
            userRepository.save(new User(registrationUsername, registrationPassword));
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
    @Loggable
    public boolean authenticateUser(String authUsername, String authPassword) {

        Optional<User> maybeUser = userRepository.findByUsername(authUsername);

        if (maybeUser.isPresent()) {
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
    @Auditable(actionType = ActionType.SHOW_RESERVATIONS)
    @Loggable
    public List<Booking> showAllReservations(String whoLogged) {

        Optional<User> user = userRepository.findByUsername(whoLogged);

        List<Booking> bookings = bookingRepository.getByUserId(user.get().getId());

        return bookings;

    }

    /**
     * Метод для получения информации о помещении по его идентификатору.
     *
     * @param id идентификатор помещения
     * @return объект Optional с информацией о помещении, если такое найдено, иначе пустой Optional
     */
    @Loggable
    public Optional<Workspace> getWorkspacesById(Long id) {
        return workspaceRepository.findById(id);
    }

    /**
     * Метод для получения пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект пользователя, если найден, иначе null
     */
    @Loggable
    public User getUserById(Long userId) {
        return userRepository.getById(userId);
    }

}
