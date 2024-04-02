package com.example.cryptoapp.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException{
    private final static String DEFAULT_MESSAGE_ID= "user with id (%s) not found";
    private final static String DEFAULT_MESSAGE_USERNAME= "user with id (%s) not found";

    public UserNotFoundException(Long id){
        super(String.format(DEFAULT_MESSAGE_ID, id));
    }

    public UserNotFoundException(String username){
        super(String.format(DEFAULT_MESSAGE_USERNAME, username));
    }

}
