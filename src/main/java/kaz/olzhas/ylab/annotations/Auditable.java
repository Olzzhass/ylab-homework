package kaz.olzhas.ylab.annotations;

import kaz.olzhas.ylab.entity.types.ActionType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация {@code Auditable} используется для пометки методов,
 * которые требуют аудита для определенных действий, выполняемых пользователями.
 * Эта аннотация позволяет отслеживать действия и соответствующего пользователя,
 * который их выполняет.
 *
 * <p>Эта аннотация сохраняется во время выполнения и может быть применена к методам.
 * Она фиксирует тип действия, подлежащего аудиту, и имя пользователя,
 * выполняющего действие.</p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * &#64;Auditable(actionType = ActionType.CREATE, username = "admin")
 * public void createResource() {
 *     // реализация метода
 * }
 * </pre>
 *
 * <p>Примечание: Атрибут {@code username} является необязательным;
 * если он не указан, будет использоваться пустая строка по умолчанию.</p>
 *
 * @see ActionType
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auditable {

    /**
     * Возвращает тип действия, подлежащего аудиту.
     *
     * @return тип действия, подлежащего аудиту
     */
    ActionType actionType();

    /**
     * Возвращает имя пользователя, выполняющего действие.
     * Если не указано, будет использоваться пустая строка по умолчанию.
     *
     * @return имя пользователя, выполняющего действие
     */
    String username() default "";
}
