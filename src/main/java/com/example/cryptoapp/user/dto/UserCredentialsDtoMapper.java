package com.example.cryptoapp.user.dto;

import com.example.cryptoapp.user.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto map(User user){
        Set<String> userRoles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRoleName())
                .collect(Collectors.toSet());
        return new UserCredentialsDto(user.getId(), user.getUsername(), user.getPassword(), userRoles, user.isLocked());
    }
}
