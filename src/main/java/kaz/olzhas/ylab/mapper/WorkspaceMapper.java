package kaz.olzhas.ylab.mapper;

import kaz.olzhas.ylab.dto.WorkspaceDto;
import kaz.olzhas.ylab.entity.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс {@code WorkspaceMapper} отвечает за преобразование между сущностью помещения {@code Workspace} и её DTO {@code WorkspaceDto}.
 * Используется библиотекой MapStruct для автоматического создания реализации маппинга.
 */
@Mapper
public interface WorkspaceMapper {

    /**
     * Преобразует сущность {@code Workspace} в DTO {@code WorkspaceDto}.
     *
     * @param workspace сущность Workspace, которую необходимо преобразовать.
     * @return соответствующий DTO WorkspaceDto.
     */
    @Mapping(target = "name", source = "name")
    WorkspaceDto toDto(Workspace workspace);

    /**
     * Преобразует DTO {@code WorkspaceDto} в сущность {@code Workspace}.
     *
     * @param workspaceDto DTO WorkspaceDto, которое необходимо преобразовать в сущность.
     * @return соответствующая сущность Workspace.
     */
    @Mapping(target = "id", ignore = true)
    Workspace toEntity(WorkspaceDto workspaceDto);
}
