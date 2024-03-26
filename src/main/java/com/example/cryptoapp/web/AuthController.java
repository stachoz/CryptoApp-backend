package com.example.cryptoapp.web;

import com.example.cryptoapp.user.UserService;
import com.example.cryptoapp.user.dto.AuthResponseDto;
import com.example.cryptoapp.user.dto.LoginDto;
import com.example.cryptoapp.user.dto.UserDto;
import com.example.cryptoapp.user.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto dto){
        UserDto userDto = userService.registerUser(dto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto dto){
        AuthResponseDto authResponseDto = userService.login(dto);
        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }
}
