package kaz.olzhas.ylab.mapper;

import kaz.olzhas.ylab.dao.UserDao;
import kaz.olzhas.ylab.dto.UserDto;
import kaz.olzhas.ylab.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    UserDto toDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto userDto);

}
