package com.example.cryptoapp.web;

import com.example.cryptoapp.exception.GlobalExceptionHandler;
import com.example.cryptoapp.exception.ValidationException;
import com.example.cryptoapp.user.UserService;
import com.example.cryptoapp.user.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private static UserRegistrationDto userRegistrationDto;
    private static UserRegistrationDto userRegistrationDtoInvalidUsername;
    private static UserRegistrationDto userRegistrationDtoInvalidEmail;
    private static UserRegistrationDto userRegistrationDtoInvalidPassword;
    @BeforeAll
    public static void init() {
        userRegistrationDto = UserRegistrationDto.builder()
                .password("user")
                .email("user@wp.pl")
                .username("user")
                .build();
        userRegistrationDtoInvalidUsername = UserRegistrationDto.builder()
                .password("")
                .email("user@wp.pl")
                .username("user")
                .build();
        userRegistrationDtoInvalidEmail = UserRegistrationDto.builder()
                .password("user")
                .email("")
                .username("user")
                .build();
        userRegistrationDtoInvalidPassword = UserRegistrationDto.builder()
                .password("user")
                .email("user@wp.pl")
                .username("")
                .build();
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        String url = "/auth/register";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldHandleValidationExceptionDuringRegistration_UserAlreadyExists() throws Exception {
        String url = "/auth/register";
        willThrow(new ValidationException(List.of())).given(userService).registerUser(any());
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleValidationExceptionDuringRegistration_InvalidUsername() throws Exception {
        String url = "/auth/register";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDtoInvalidUsername)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("validation failed"));
    }

    @Test
    public void shouldHandleValidationExceptionDuringRegistration_InvalidEmail() throws Exception {
        String url = "/auth/register";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDtoInvalidEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("validation failed"));
    }

    @Test
    public void shouldHandleValidationExceptionDuringRegistration_InvalidPassword() throws Exception {
        String url = "/auth/register";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDtoInvalidPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("validation failed"));
    }

    @Test
    public void shouldHandleEmptyBodyWhileRegistration() throws Exception {
        String url = "/auth/register";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request body is missing"));
    }
}