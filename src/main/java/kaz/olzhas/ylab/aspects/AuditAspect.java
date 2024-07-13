package kaz.olzhas.ylab.aspects;

import kaz.olzhas.ylab.annotations.Auditable;
import kaz.olzhas.ylab.entity.types.ActionType;
import kaz.olzhas.ylab.service.AuditService;
import kaz.olzhas.ylab.state.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Аспект {@code AuditAspect} отвечает за аудит действий, помеченных аннотацией {@code @Auditable}.
 *
 * <p>Этот аспект перехватывает выполнение методов, помеченных аннотацией {@code @Auditable},
 * извлекает информацию о действии и пользователе, выполняющем действие, и записывает
 * соответствующую информацию через сервис аудита.</p>
 *
 * <p>Для работы аспекта необходимы компоненты {@link AuditService} для записи аудита
 * и {@link Authentication} для получения информации о текущем пользователе.</p>
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final Authentication authentication;

    /**
     * Точка входа для методов, помеченных аннотацией {@code @Auditable}.
     *
     * @param auditable Аннотация {@code @Auditable}, определяющая тип действия
     */
    @Pointcut("@annotation(kaz.olzhas.ylab.annotations.Loggable) && @annotation(auditable)")
    public void annotatedByAuditable(Auditable auditable) { }

    /**
     * Метод аспекта, выполняющий аудит для методов, помеченных аннотацией {@code @Auditable}.
     *
     * @param joinPoint Точка присоединения для текущего выполнения метода
     * @param auditable Аннотация {@code @Auditable}, определяющая тип действия
     * @return Результат выполнения метода, который был перехвачен аспектом
     * @throws Throwable Исключение, которое может быть брошено во время выполнения метода
     */
    @Around("annotatedByAuditable(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Auditable auditAnnotation = methodSignature.getMethod().getAnnotation(Auditable.class);

        String payload = authentication.getUsername();
        ActionType actionType = auditAnnotation.actionType();

        Object result = joinPoint.proceed();
        auditService.audit(payload, actionType, LocalDateTime.now());
        return result;
    }
}
