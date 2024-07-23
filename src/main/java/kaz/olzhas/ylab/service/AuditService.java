package kaz.olzhas.ylab.service;

import kaz.olzhas.ylab.entity.Audit;
import kaz.olzhas.ylab.entity.types.ActionType;
import kaz.olzhas.ylab.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;

    /**
     * Метод для сохранения записи аудита.
     *
     * @param audit запись аудита для сохранения
     * @return true, если сохранение успешно, иначе false
     */
    public boolean save(Audit audit){
        return auditRepository.save(audit);
    }

    /**
     * Метод для получения всех записей аудита.
     *
     * @return список всех записей аудита
     */
    public List<Audit> findAllAudits(){
        return auditRepository.findAll();
    }

    /**
     * Метод для выполнения аудита действия пользователя.
     *
     * @param username имя пользователя
     * @param actionType тип действия
     * @param time время аудита
     * @return true, если аудит выполнен успешно, иначе false
     */
    public boolean audit(String username, ActionType actionType, LocalDateTime time) {

        Audit audit = new Audit();
        audit.setUsername(username);
        audit.setActionType(actionType);
        audit.setAuditTime(time);

        return save(audit);
    }
}
