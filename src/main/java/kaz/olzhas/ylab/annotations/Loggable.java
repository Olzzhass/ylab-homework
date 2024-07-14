package kaz.olzhas.ylab.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация {@code Loggable} используется для пометки методов или классов,
 * которые требуют логирования выполнения операций.
 *
 * <p>Эта аннотация позволяет обозначить методы или классы, для которых необходимо
 * вести лог действий, выполняемых в процессе их работы.</p>
 *
 * <p>Аннотация может быть применена как к методам, так и к классам.</p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * &#64;Loggable
 * public class UserService {
 *     &#64;Loggable
 *     public void createUser(User user) {
 *         // реализация метода
 *     }
 * }
 * </pre>
 *
 * <p>Логирование может включать запись важных событий, параметров методов и времени их выполнения.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Loggable {
}
