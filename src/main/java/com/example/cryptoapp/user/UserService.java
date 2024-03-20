package com.example.cryptoapp.user;

import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.exception.UserNotFoundException;
import com.example.cryptoapp.exception.ValidationException;
import com.example.cryptoapp.user.dto.*;
import com.example.cryptoapp.user.repos.UserRepository;
import com.example.cryptoapp.user.repos.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRegistrationDtoMapper userRegistrationDtoMapper;
    private final String USER_ROLE = "USER";
    private final String ADMIN_ROLE = "ADMIN";

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, UserRegistrationDtoMapper userRegistrationDtoMapper){
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRegistrationDtoMapper = userRegistrationDtoMapper;
    }

    public Optional<UserCredentialsDto> findCredentialsByUsername(String username){
        return userRepository.findByUsername(username).map(
                UserCredentialsDtoMapper::map
        );
    }

    public List<UserDto> getAllUsers(PageRequest pageRequest) {
        Page<User> pageOfUsers = userRepository.findAll(pageRequest);
        int pageNumber = pageRequest.getPageNumber();
        if(pageNumber != 0 && pageNumber + 1 > pageOfUsers.getTotalPages()) throw new NoSuchElementException("page " + pageNumber + " is out of bounds");
        return pageOfUsers.stream()
                .map(UserDtoMapper::map)
                .collect(Collectors.toList());
    }

    public UserDto registerUser(UserRegistrationDto dto){
        User user = userRegistrationDtoMapper.map(dto);
        List<String> errors = checkUserCredentials(user);
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }
        if(!userRepository.hasAnyRows()){
            UserRole adminRole = userRoleRepository.findUserRoleByRoleName(ADMIN_ROLE)
                    .orElse(new UserRole(ADMIN_ROLE));
            user.setUserRoles(Set.of(adminRole));
        } else {
            UserRole userRole = userRoleRepository.findUserRoleByRoleName(USER_ROLE)
                    .orElse(new UserRole(USER_ROLE));
            user.setUserRoles(Set.of(userRole));
        }
        User savedUser = userRepository.save(user);
        return UserDtoMapper.map(savedUser);
    }

    public void deleteUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if(isUserAdmin(user)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserDto getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
        return UserDtoMapper.map(user);
    }

    public void changeUserAccess(Long userId, UserOperationDto dto){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if(isUserAdmin(user)) throw new UserNotFoundException(user.getId());
        switch (dto.getOperation()){
            case BLOCK -> blockUser(user);
            case UNBLOCK -> unblockUser(user);
            case ENABLE_POST_VERIFICATION -> enablePostVerification(user);
            case DISABLE_POST_VERIFICATION -> disablePostVerification(user);
        }
        userRepository.save(user);
    }
    private List<String> checkUserCredentials(User user){
        List<String> errorMessages = new ArrayList<>();
        String username = user.getUsername();
        String email = user.getEmail();
        if(userRepository.existsUserByUsername(username)){
            errorMessages.add("username not unique");
        }
        if(userRepository.existsUserByEmail(email)){
            errorMessages.add("email not unique");
        }
        return errorMessages;
    }

    private void blockUser(User user){
        if(user.isLocked()) throw new OperationConflictException("user with id <" + user.getId() + "> is already blocked");
        user.setLocked(true);
    }

    private void unblockUser(User user){
        if(!user.isLocked()) throw new OperationConflictException("user with id <" + user.getId() + "> is already unblocked");
        user.setLocked(false);
    }
    private void enablePostVerification(User user){
        if(user.isPostVerification()) throw new OperationConflictException("user with id <" + user.getId() + "> has already enabled post verification");
        user.setPostVerification(true);
    }

    private void disablePostVerification(User user){
        if(!user.isPostVerification()) throw new OperationConflictException("user with id <" + user.getId() + "> has already disabled post verification");
        user.setPostVerification(false);
    }

    private boolean isUserAdmin(User user){
        return user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRoleName().equals(ADMIN_ROLE));
    }
}
