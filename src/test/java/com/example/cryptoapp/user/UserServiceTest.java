package com.example.cryptoapp.user;

import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.exception.UserNotFoundException;
import com.example.cryptoapp.user.dto.UserDto;
import com.example.cryptoapp.user.dto.UserOperationDto;
import com.example.cryptoapp.user.dto.UserRegistrationDto;
import com.example.cryptoapp.user.dto.UserRegistrationDtoMapper;
import com.example.cryptoapp.user.repos.UserRepository;
import com.example.cryptoapp.user.repos.UserRoleRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRegistrationDtoMapper userRegistrationDtoMapper;
    @InjectMocks
    private UserService userService;
    private User validUser;
    private User adminUser;
    private UserRegistrationDto userRegistrationDto;
    private UserDto userDto;
    private UserDto adminDto;
    private UserRegistrationDto adminRegistrationDto;
    private final static String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    @BeforeEach
    void init(){

        validUser = User.builder()
                .id(2L)
                .username("test")
                .email("test@wp.pl")
                .userRoles(Set.of(new UserRole(USER_ROLE))).build();
        adminUser = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@wp.pl")
                .userRoles(Set.of(new UserRole(ADMIN_ROLE))).build();
        userRegistrationDto = UserRegistrationDto.builder()
                .username("test")
                .email("test@wp.pl")
                .password("test123").build();
        userDto = UserDto.builder()
                .id(2L)
                .username("test")
                .email("test@wp.pl")
                .rolesNames(Set.of(USER_ROLE))
                .build();
        adminDto = UserDto.builder()
                .id(1L)
                .username("admin")
                .email("admin@wp.pl")
                .rolesNames(Set.of(ADMIN_ROLE))
                .build();
        adminRegistrationDto = UserRegistrationDto.builder()
                .username("admin")
                .email("admin@wp.pl")
                .password("admin123").build();
    }

    @Test
    public void shouldReturnUserById(){
        when(userRepository.findById(any())).thenReturn(Optional.of(validUser));
        UserDto dto = userService.getUserById(2L);
        assertThat(dto).isEqualTo(userDto);
    }

    @Test
    public void shouldThrowExceptionForNotExistingUser(){
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }


    @Test
    public void deleteValidUser(){
        Long id = validUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(validUser));
        userService.deleteUserById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingAdmin(){
        Long id = adminUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(adminUser));
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    public void testDeleteNonExistingUser(){
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1000L));
    }

    @Test
    public void shouldRegisterUser(){
        when(userRegistrationDtoMapper.map(userRegistrationDto)).thenReturn(validUser);
        when(userRepository.hasAnyRows()).thenReturn(false);
        when(userRoleRepository.findUserRoleByRoleName(USER_ROLE)).thenReturn(Optional.of(new UserRole(USER_ROLE)));
        when(userRepository.save(any())).thenReturn(validUser);

        UserDto savedUserDto = userService.registerUser(userRegistrationDto);
        assertEquals(userDto.getId(), savedUserDto.getId());
        assertEquals(userDto.getUsername(), savedUserDto.getUsername());
        assertEquals(userDto.getEmail(), savedUserDto.getEmail());
        assertEquals(userDto.getRolesNames(), savedUserDto.getRolesNames());
    }

    @Test
    public void shouldRegisterAdmin(){
        when(userRegistrationDtoMapper.map(adminRegistrationDto)).thenReturn(adminUser);
        when(userRepository.hasAnyRows()).thenReturn(true);
        when(userRoleRepository.findUserRoleByRoleName(ADMIN_ROLE)).thenReturn(Optional.of(new UserRole(ADMIN_ROLE)));
        when(userRepository.save(any())).thenReturn(adminUser);

        UserDto savedUserDto = userService.registerUser(adminRegistrationDto);
        assertEquals(adminDto.getId(), savedUserDto.getId());
        assertEquals(adminDto.getUsername(), savedUserDto.getUsername());
        assertEquals(adminDto.getEmail(), savedUserDto.getEmail());
        assertEquals(adminDto.getRolesNames(), savedUserDto.getRolesNames());
    }
    @Test
    public void registerUser_nonUniqueUsername_throwsValidationException() {
        UserRegistrationDto dto = new UserRegistrationDto("existingUsername", "email", "password");
        when(userRegistrationDtoMapper.map(dto))
                .thenReturn(User.builder()
                        .username("existingUsername")
                        .email("email")
                        .password("password").build()
                );
        when(userRepository.existsUserByUsername("existingUsername")).thenReturn(true);

        assertThrows(ValidationException.class, () -> userService.registerUser(dto));
    }

    @Test
    public void registerUser_nonUniqueEmail_throwsValidationException() {
        UserRegistrationDto dto = new UserRegistrationDto("username", "existing@mail.com", "password");
        when(userRegistrationDtoMapper.map(dto))
                .thenReturn(User.builder()
                        .username("username")
                        .email("existing@mail.com")
                        .password("password").build()
                );
        when(userRepository.existsUserByEmail("existing@mail.com")).thenReturn(true);
        assertThrows(ValidationException.class, () -> userService.registerUser(dto));
    }

    @Test
    public void shouldBlockUser(){
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.BLOCK));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.isLocked()).isTrue();
    }

    @Test
    public void shouldUnBlockUser(){
        validUser.setLocked(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.UNBLOCK));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.isLocked()).isFalse();
    }
    @Test
    public void shouldEnableVerification(){
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.ENABLE_POST_VERIFICATION));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.isPostVerification()).isTrue();
    }
    @Test
    public void shouldDisableVerification(){
        validUser.setPostVerification(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.DISABLE_POST_VERIFICATION));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.isPostVerification()).isFalse();
    }
    @Test
    public void shouldThrowException_BlockBlockedUser(){
        validUser.setLocked(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        assertThrows(OperationConflictException.class, () -> userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.BLOCK)));
    }
    @Test
    public void shouldThrowException_UnBlockUnBlockedUser(){
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        assertThrows(OperationConflictException.class, () -> userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.UNBLOCK)));
    }

    @Test
    public void shouldThrowException_disableVerificationForUserWithDisabledVerification(){
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        assertThrows(OperationConflictException.class, () -> userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.DISABLE_POST_VERIFICATION)));
    }

    @Test
    public void shouldThrowException_enableVerificationForUserWithDisabledVerification(){
        validUser.setPostVerification(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(validUser));
        assertThrows(OperationConflictException.class, () -> userService.changeUserAccess(2L, new UserOperationDto(UserOperationEnum.ENABLE_POST_VERIFICATION)));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionForAdminUSer(){
        when(userRepository.findById(any())).thenReturn(Optional.of(adminUser));
        assertThrows(UserNotFoundException.class, () -> userService.changeUserAccess(1L, any()));
    }

    @Test
    public void shouldReturnListOfUsers(){
        List<User> users = List.of(validUser, validUser);
        Page<User> mockPage = new PageImpl<>(users, PageRequest.of(1, 10), 1);
        when(userRepository.findAll(any())).thenReturn(mockPage);
        List<UserDto> allUsers = userService.getAllUsers(PageRequest.of(1, 10));
        assertThat(allUsers.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnSecondPage(){
        List<User> users = List.of(validUser, validUser, validUser, validUser, validUser, validUser);
        Page<User> mockPage = new PageImpl<>(users, PageRequest.of(0, 3), 2);
        when(userRepository.findAll(any())).thenReturn(mockPage);
        assertThrows(NoSuchElementException.class, () -> userService.getAllUsers(PageRequest.of(2, 3)));
    }

    @Test
    public void shouldThrowExceptionWhenPageOutOfBounds(){
        List<User> users = List.of(validUser, validUser, validUser, validUser, validUser, validUser);
        Page<User> mockPage = new PageImpl<>(users, PageRequest.of(0, 3), 2);
        when(userRepository.findAll(any())).thenReturn(mockPage);
        assertThrows(NoSuchElementException.class, () -> userService.getAllUsers(PageRequest.of(3, 3)));
    }

    @Test
    public void shouldReturnEmptyList_WhenThereIsNoUser(){
        PageImpl<User> mockPage = new PageImpl<>(List.of());
        when(userRepository.findAll(PageRequest.of(0, 3))).thenReturn(mockPage);
        List<UserDto> users = userService.getAllUsers(PageRequest.of(0, 3));
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void shouldThrowException_PageOutOfBounds_WhenThereIsNoUserAndRequestNotFirstPage(){
        PageImpl<User> mockPage = new PageImpl<>(List.of(), PageRequest.of(0, 3), 0);
        when(userRepository.findAll(PageRequest.of(1, 3))).thenReturn(mockPage);
        assertThrows(NoSuchElementException.class, () -> userService.getAllUsers(PageRequest.of(1, 3)));
    }
}
