package com.example.cryptoapp.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException{
    List<String> errors;
    public ValidationException(List<String> errors){
        super();
        this.errors = errors;
    }
}
