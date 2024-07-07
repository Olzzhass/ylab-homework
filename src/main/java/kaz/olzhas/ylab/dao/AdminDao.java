package kaz.olzhas.ylab.dao;

import kaz.olzhas.ylab.entity.Admin;
import kaz.olzhas.ylab.entity.User;

import java.util.Optional;

public interface AdminDao {
    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска.
     * @return Optional, содержащий найденного пользователя, или пустой Optional, если пользователь не найден.
     */
    Optional<Admin> findByUsername(String username);
}
