package kaz.olzhas.ylab.repository;

import kaz.olzhas.ylab.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository {

    /**
     * Находит пользователя по его имени пользователя (username).
     *
     * @param username имя пользователя для поиска.
     * @return Optional с найденным пользователем или пустой Optional, если пользователь не найден.
     */
    Optional<User> findByUsername(String username);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей.
     */
    List<User> findAll();

    /**
     * Сохраняет нового пользователя.
     *
     * @param user пользователь для сохранения.
     * @return true, если пользователь сохранен успешно, false в противном случае.
     */
    boolean save(User user);

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return найденный пользователь или null, если пользователь не найден.
     */
    User getById(Long userId);
}
