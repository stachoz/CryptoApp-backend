package com.example.cryptoapp.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String username;
    private String email;
    @JsonProperty("roles")
    private Set<String> rolesNames;
}
