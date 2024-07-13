package kaz.olzhas.ylab.repository;

import kaz.olzhas.ylab.entity.Audit;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с аудитными записями в системе.
 */
public interface AuditRepository {

    /**
     * Находит аудитную запись по её идентификатору.
     *
     * @param id идентификатор аудитной записи.
     * @return Optional с найденной аудитной записью, если она существует, иначе пустой Optional.
     */
    Optional<Audit> findById(Long id);

    /**
     * Возвращает список всех аудитных записей в системе.
     *
     * @return список всех аудитных записей.
     */
    List<Audit> findAll();

    /**
     * Сохраняет новую аудитную запись в системе.
     *
     * @param audit аудитная запись для сохранения.
     * @return true, если сохранение прошло успешно, false в противном случае.
     */
    boolean save(Audit audit);

}
