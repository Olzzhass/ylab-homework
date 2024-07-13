package kaz.olzhas.ylab.repository;

import kaz.olzhas.ylab.entity.Admin;
import java.util.Optional;

/**
 * Репозиторий для работы с администраторами в системе.
 */
public interface AdminRepository {

    /**
     * Ищет администратора по имени пользователя.
     *
     * @param username имя пользователя администратора.
     * @return Optional с найденным администратором, если он существует, иначе пустой Optional.
     */
    Optional<Admin> findByUsername(String username);
}
