package kaz.olzhas.ylab.repository;

import kaz.olzhas.ylab.entity.Workspace;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с помещениями (Workspace).
 */
public interface WorkspaceRepository {

    /**
     * Возвращает список всех помещений.
     *
     * @return список всех помещений.
     */
    List<Workspace> findAll();

    /**
     * Находит помещение по его идентификатору.
     *
     * @param id идентификатор помещения.
     * @return Optional с найденным помещением или пустой Optional, если помещение не найдено.
     */
    Optional<Workspace> findById(Long id);

    /**
     * Сохраняет новое помещение.
     *
     * @param workspace помещение для сохранения.
     * @return true, если помещение сохранено успешно, false в противном случае.
     */
    boolean save(Workspace workspace);
}
