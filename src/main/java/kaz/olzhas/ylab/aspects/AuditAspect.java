package kaz.olzhas.ylab.aspects;

import kaz.olzhas.ylab.annotations.Auditable;
import kaz.olzhas.ylab.dao.AuditLogDao;
import kaz.olzhas.ylab.entity.AuditLog;
import kaz.olzhas.ylab.state.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.LocalDateTime;

@Aspect
@Slf4j
public class AuditAspect {

    private AuditLogDao auditLogDao;

    public AuditAspect(AuditLogDao auditLogDao){
        this.auditLogDao = auditLogDao;
    }

    @Pointcut("@annotation(kaz.olzhas.ylab.annotations.Auditable)")
    public void annotatedByAuditable() { }


    @Around("annotatedByAuditable()")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        String methodName = methodSignature.getMethod().getName();
        String username = audit.username(); // Replace with actual logic to get the current username

        log.info("User " + username + " is calling method " + methodName);

        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction("Called method: " + methodName);
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogDao.save(auditLog);

        return joinPoint.proceed();
    }
}
