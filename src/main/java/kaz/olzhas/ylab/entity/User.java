package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность {@code User} представляет информацию о пользователе системы.
 *
 * <p>Класс содержит поля, описывающие идентификатор пользователя ({@code id}), имя пользователя ({@code username}) и пароль ({@code password}).</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;

    /**
     * Конструктор для создания объекта пользователя с указанием имени и пароля.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
