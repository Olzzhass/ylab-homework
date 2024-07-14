package kaz.olzhas.ylab.mapper;

import kaz.olzhas.ylab.dto.UserDto;
import kaz.olzhas.ylab.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс {@code UserMapper} отвечает за преобразование между сущностью пользователя {@code User} и её DTO {@code UserDto}.
 * Используется библиотекой MapStruct для автоматического создания реализации маппинга.
 */
@Mapper
public interface UserMapper {

    /**
     * Преобразует сущность {@code User} в DTO {@code UserDto}.
     *
     * @param entity сущность User, которую необходимо преобразовать.
     * @return соответствующий DTO UserDto.
     */
    @Mapping(target = "username", source = "username")
    UserDto toDto(User entity);

    /**
     * Преобразует DTO {@code UserDto} в сущность {@code User}.
     *
     * @param userDto DTO UserDto, которое необходимо преобразовать в сущность.
     * @return соответствующая сущность User.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto userDto);
}
