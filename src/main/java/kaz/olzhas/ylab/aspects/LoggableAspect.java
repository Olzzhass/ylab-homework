package kaz.olzhas.ylab.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Аспект {@code LoggableAspect} отвечает за логирование вызовов методов,
 * помеченных аннотацией {@code @Loggable}.
 *
 * <p>Этот аспект перехватывает выполнение методов, помеченных аннотацией {@code @Loggable},
 * и записывает информацию о вызове метода в лог, включая его имя и время выполнения.</p>
 */
@Aspect
@Slf4j
@Component
public class LoggableAspect {

    /**
     * Точка входа для методов, помеченных аннотацией {@code @Loggable}.
     */
    @Pointcut("@annotation(kaz.olzhas.ylab.annotations.Loggable) && execution(* *(..))")
    public void annotatedByLoggable() { }

    /**
     * Метод аспекта, выполняющий логирование для методов, помеченных аннотацией {@code @Loggable}.
     *
     * @param proceedingJoinPoint Точка присоединения для текущего выполнения метода
     * @return Результат выполнения метода, который был перехвачен аспектом
     * @throws Throwable Исключение, которое может быть брошено во время выполнения метода
     */
    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().toShortString();
        log.info("Calling method " + methodName);

        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        log.info("Execution of method " + methodName +
                " finished. Execution time is " + (endTime - startTime) + " ms.");
        return result;
    }
}
