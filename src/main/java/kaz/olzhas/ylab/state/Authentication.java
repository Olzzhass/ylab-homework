package kaz.olzhas.ylab.state;

import lombok.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Представляет состояние аутентификации пользователя.
 * Этот класс управляет именем пользователя и состоянием аутентификации.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
@Setter
public class Authentication {
    private String username;
    private boolean isAuth;

    /**
     * Проверяет, является ли аутентифицированный пользователь администратором.
     *
     * @return true, если пользователь аутентифицирован и является администратором, иначе false
     */
    public boolean isAdmin(){
        return isAuth && "Admin".equalsIgnoreCase(username);
    }
}
