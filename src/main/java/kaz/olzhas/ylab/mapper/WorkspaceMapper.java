package kaz.olzhas.ylab.mapper;

import kaz.olzhas.ylab.dto.WorkspaceDto;
import kaz.olzhas.ylab.entity.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WorkspaceMapper {
    @Mapping(target = "name", source = "name")
    WorkspaceDto toDto(Workspace workspace);

    @Mapping(target = "id", ignore = true)
    Workspace toEntity(WorkspaceDto workspaceDto);
}
