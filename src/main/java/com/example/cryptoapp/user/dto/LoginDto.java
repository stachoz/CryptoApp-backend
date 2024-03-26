package com.example.cryptoapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank
    @Size(min = 4, max = 100)
    private String username;
    @NotBlank
    @Size(min = 4, max = 100)
    private String password;
}
