package com.example.cryptoapp.exception;

public class OperationConflictException extends RuntimeException{
    public OperationConflictException(String message){
        super(message);
    }
}
