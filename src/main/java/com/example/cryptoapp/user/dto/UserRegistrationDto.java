package com.example.cryptoapp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {
    @NotNull(message = "can not be null")
    @Size(min = 3, max = 50)
    private String username;
    @NotNull(message = "can not be null")
    @Size(min = 5, max = 80)
    @Email
    private String email;
    @NotNull(message = "can not be null")
    @Size(min = 4, max = 200)
    private String password;
}
