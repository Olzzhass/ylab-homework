package kaz.olzhas.ylab.aspects;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@Slf4j
public class LoggableAspect {

    @Pointcut("within(@kaz.olzhas.ylab.annotations.Loggable) && execution(* *(..))")
    public void annotatedByLoggable() { }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Calling method " + methodSignature.toShortString());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        System.out.println("LOGGABLE WORKS");

        stopWatch.stop();
        log.info("Execution of method " + methodSignature.toShortString() +
                " finished. Execution time is " + (endTime - startTime) + " ms");

        log.info("Method: " + methodSignature.toShortString() + " executed. Execution time - " + (endTime - startTime) + ".ms");
        return result;
    }
}
