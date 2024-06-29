package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс пользователя, содержащий простые поля username и password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;

    /**
     * Конструктор для создания пользователя с указанием username и password.
     *
     * @param username имя пользователя.
     * @param password пароль пользователя.
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
