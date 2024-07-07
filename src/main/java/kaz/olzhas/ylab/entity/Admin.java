package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long id;
    private String adminName;
    private String adminPassword;

    /**
     * Конструктор для создания пользователя с указанием username и password.
     *
     * @param adminName имя пользователя.
     * @param adminPassword пароль пользователя.
     */
    public Admin(String adminName, String adminPassword){
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }
}
