package com.example.cryptoapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDto {
    private Long id;
    private String username;
    private String password;
    private Set<String> roles;
    private boolean isLocked;
}
