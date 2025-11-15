package by.Shelden.util;

import by.Shelden.dto.UserDto;
import by.Shelden.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity dtoToEntity(UserDto userDto){
        return new UserEntity(
                userDto.name(),
                userDto.email(),
                userDto.age());
    }

    public UserDto entityToDto(UserEntity userEntity){
        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getAge(),
                userEntity.getCreatedAt());
    }
}
