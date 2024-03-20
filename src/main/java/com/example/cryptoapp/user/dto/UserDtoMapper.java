package com.example.cryptoapp.user.dto;

import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserRole;

import java.util.stream.Collectors;

public class UserDtoMapper {
    public static UserDto map(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRolesNames(user.getUserRoles()
                .stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toSet())
        );
        return userDto;
    }
}
