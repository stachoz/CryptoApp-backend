package com.example.cryptoapp.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException{
    private final static String DEFAULT_MESSAGE= "user with id (%s) not found";

    public UserNotFoundException(Long id){
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
