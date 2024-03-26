package com.example.cryptoapp.security;

import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserRole;
import com.example.cryptoapp.user.UserService;
import com.example.cryptoapp.user.repos.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> createUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username with email %s not found", username)));
    }

    private UserDetails createUserDetails(User u){
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .roles(u.getUserRoles().stream()
                        .map(UserRole::getRoleName)
                        .toArray(String[]::new))
                .accountLocked(u.isLocked())
                .build();
    }
}
