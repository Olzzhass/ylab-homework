package kaz.olzhas.ylab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность {@code Admin} представляет администратора системы.
 *
 * <p>Класс содержит основные поля и методы для работы с данными администратора.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long id;
    private String adminName;
    private String adminPassword;

    /**
     * Конструктор для создания администратора с указанием имени и пароля.
     *
     * @param adminName имя администратора.
     * @param adminPassword пароль администратора.
     */
    public Admin(String adminName, String adminPassword){
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }
}
