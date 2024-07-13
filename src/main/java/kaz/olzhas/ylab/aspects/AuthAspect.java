package kaz.olzhas.ylab.aspects;

import kaz.olzhas.ylab.annotations.CheckAuth;
import kaz.olzhas.ylab.exception.AuthorizeException;
import kaz.olzhas.ylab.state.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Аспект {@code AuthAspect} отвечает за проверку авторизации перед выполнением методов,
 * помеченных аннотацией {@code @CheckAuth}.
 *
 * <p>Этот аспект проверяет, имеет ли текущий пользователь права доступа для выполнения
 * метода, в зависимости от установленных аннотацией условий. Если проверка не пройдена,
 * выбрасывается исключение {@link AuthorizeException} с соответствующим сообщением об ошибке.</p>
 *
 * <p>Для работы аспекта необходим компонент {@link Authentication}, который предоставляет
 * информацию о текущем состоянии аутентификации пользователя.</p>
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthAspect {

    private final Authentication authentication;

    /**
     * Метод аспекта, выполняющий проверку авторизации перед выполнением методов,
     * помеченных аннотацией {@code @CheckAuth}.
     *
     * @param checkAuth Аннотация {@code @CheckAuth}, определяющая требования к авторизации
     * @throws AuthorizeException Исключение, выбрасываемое при неудачной проверке авторизации
     */
    @Before("@annotation(checkAuth)")
    public void checkAuthorization(CheckAuth checkAuth) {
        if (checkAuth.admin()) {
            if (!authentication.isAdmin()) {
                throw new AuthorizeException("Только админы имеют доступ");
            }
        } else {
            if (!authentication.isAuth()) {
                throw new AuthorizeException("Сначало нужно войти в приложени");
            }
        }
    }
}
