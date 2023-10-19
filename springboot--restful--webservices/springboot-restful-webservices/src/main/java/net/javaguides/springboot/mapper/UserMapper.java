package net.javaguides.springboot.mapper;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.entity.User;

public class UserMapper {

    public static UserDto maptoUserDto(User user){
        UserDto changedUser = new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
                );
        return changedUser;
    }

    public static User maptoUser(UserDto user){
        User changedUser = new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
        return changedUser;
    }
}
