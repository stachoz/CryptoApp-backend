package com.example.cryptoapp.web;

import com.example.cryptoapp.user.UserService;
import com.example.cryptoapp.user.dto.UserDto;
import com.example.cryptoapp.user.dto.UserOperationDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        UserDto dto = userService.getUserById(id);
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/{id}/access")
    ResponseEntity<?> changeUserAccess(@Valid @RequestBody UserOperationDto dto, @PathVariable Long id){
        userService.changeUserAccess(id, dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/list")
    ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "username", required = false) String username){
        if(username == null){
            List<UserDto> allUsers = userService.getAllUsers(PageRequest.of(page, size));
            return ResponseEntity.ok(allUsers);
        }
        List<UserDto> userByUsername = List.of(userService.getUserByUsername(username));
        return new ResponseEntity<>(userByUsername, HttpStatus.OK);
    }
}
