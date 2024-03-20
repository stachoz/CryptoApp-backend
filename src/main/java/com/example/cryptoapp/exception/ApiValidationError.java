package com.example.cryptoapp.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ApiValidationError extends ApiError{
    private List<String> errors;

    public ApiValidationError(String message, LocalDateTime timestamp, String path, List<String> errors) {
        super(message, timestamp, path);
        this.errors = errors;
    }
}
