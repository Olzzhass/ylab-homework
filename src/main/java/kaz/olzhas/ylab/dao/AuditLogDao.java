package kaz.olzhas.ylab.dao;

import kaz.olzhas.ylab.entity.AuditLog;

public interface AuditLogDao {
    void save(AuditLog auditLog);
}
