package com.example.cryptoapp.web;

import com.example.cryptoapp.enums.UserOperationEnum;
import com.example.cryptoapp.exception.GlobalExceptionHandler;
import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.exception.UserNotFoundException;
import com.example.cryptoapp.user.UserService;
import com.example.cryptoapp.user.dto.UserDto;
import com.example.cryptoapp.user.dto.UserOperationDto;
import com.example.cryptoapp.user.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    private static UserDto userDto;
    private static UserDto adminDto;
    private static UserRegistrationDto userRegistrationDto;
    @BeforeAll
    public static void init(){
        userRegistrationDto = UserRegistrationDto.builder()
                .password("user")
                .email("user@wp.pl")
                .username("user")
                .build();
        userDto = UserDto.builder()
                .id(2L)
                .username("user")
                .email("user@wp.pl")
                .rolesNames(Set.of("USER"))
                .build();
        adminDto = UserDto.builder()
                .id(1L)
                .username("admin")
                .email("amdin@wp.pl")
                .rolesNames(Set.of("ADMIN"))
                .build();
    }

    @Test
    public void shouldReturnStatusOkOnEmptyList() throws Exception {
        String url = "/user/list";
        List<UserDto> users = new ArrayList<>();
        given(userService.getAllUsers(any())).willReturn(users);
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        String url = "/user/list";
        List<UserDto> users = List.of(adminDto, userDto, userDto);
        given(userService.getAllUsers(any())).willReturn(users);
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    public void shouldThrowExceptionWhenPageIsOutOfBounds() throws Exception {
        String url = "/user/list";
        given(userService.getAllUsers(any())).willThrow(NoSuchElementException.class);
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatusOkAfterDeleteUser() throws Exception {
        String url = "/user/1";
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.blankString()));
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNotExistingUserOrAdmin() throws Exception {
        String url = "/user/1";
        willThrow(new UserNotFoundException()).given(userService).deleteUserById(anyLong());
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        String url = "/user";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldHandleValidationExceptionDuringRegistration_UserAlreadyExists() throws Exception {
        String url = "/user";
        willThrow(new ValidationException()).given(userService).registerUser(any());
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldChangeUserAccess() throws Exception {
        int userId = 1000;
        String url = "/user/" + userId + "/access";
        UserOperationDto dto = new UserOperationDto(UserOperationEnum.BLOCK);
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
    @Test
    public void shouldHandleException_EmptyBody() throws Exception {
        int userId = 1000;
        String url = "/user/" + userId + "/access";
        UserOperationDto dto = new UserOperationDto();
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void shouldHandleException_BadOperation() throws Exception {
        int userId = 1000;
        String url = "/user/" + userId + "/access";
        String badOperationJson = "{\"operation\":\"unknown\"}";
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badOperationJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleException_OperationConflict() throws Exception {
        int userId = 1000;
        String url = "/user/" + userId + "/access";
        UserOperationDto dto = new UserOperationDto(UserOperationEnum.BLOCK);
        willThrow(OperationConflictException.class).given(userService).changeUserAccess(any(), any());
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnUserJsonAndOk() throws Exception {
        Long id = 1L;
        String url = "/user/" + id;
        given(userService.getUserById(any())).willReturn(userDto);
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void shouldHandleUserNotFound() throws Exception {
        Long id = 1L;
        String url = "/user/" + id;
        willThrow(UserNotFoundException.class).given(userService).getUserById(any());
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
