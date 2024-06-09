package com.example.cryptoapp.user.dto;

import com.example.cryptoapp.user.UserOperationEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOperationDto {
    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonProperty("operation")
    private UserOperationEnum operation;
}
