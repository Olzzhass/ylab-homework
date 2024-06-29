package kaz.olzhas.ylab.dao;

import kaz.olzhas.ylab.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для взаимодействия с пользователями в базе данных.
 */
public interface UserDao {
    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска.
     * @return Optional, содержащий найденного пользователя, или пустой Optional, если пользователь не найден.
     */
    Optional<User> findByUsername(String username);

    /**
     * Извлекает всех пользователей из базы данных.
     *
     * @return список всех пользователей.
     */
    List<User> findAll();

    /**
     * Сохраняет пользователя в базе данных.
     *
     * @param user пользователь для сохранения.
     * @return true, если пользователь был успешно сохранен, иначе false.
     */
    boolean save(User user);

    /**
     * Извлекает пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя для поиска.
     * @return пользователь с указанным идентификатором или null, если пользователь не найден.
     */
    User getById(Long userId);
}
