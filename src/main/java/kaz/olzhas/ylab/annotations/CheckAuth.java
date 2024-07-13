package kaz.olzhas.ylab.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация {@code CheckAuth} используется для указания необходимости проверки
 * аутентификации пользователя перед выполнением метода.
 *
 * <p>Атрибут {@code admin} указывает, требуется ли проверка аутентификации для администратора.
 * Если {@code admin} установлен в {@code true}, проверяется аутентификация администратора.</p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * &#64;CheckAuth(admin = true)
 * public void showAllUsers() {
 *     // реализация метода
 * }
 * </pre>
 *
 * <p>Если атрибут {@code admin} не указан, по умолчанию используется значение {@code false}.</p>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CheckAuth {

    /**
     * Указывает, требуется ли проверка аутентификации для администратора.
     *
     * @return {@code true}, если требуется проверка аутентификации для администратора, иначе {@code false}
     */
    boolean admin() default false;
}
