package com.example.cryptoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final String ADMIN_ROLE = "ADMIN";
        final String USER_ROLE = "USER";

        return http
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.PUT, "/user/*/access").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/user/*").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/post").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/post/*").hasAnyRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/post/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/post/*/report").authenticated()
                        .requestMatchers(HttpMethod.POST, "/post/*/verify").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, "/post/*/comment").authenticated()
                        .requestMatchers(HttpMethod.GET , "/post/*/comment/list").permitAll()
                        .requestMatchers(HttpMethod.DELETE , "/post/*/comment/*").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/post/*/report/list").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/post/*/report/*").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/coins").permitAll()
                        .anyRequest().denyAll()
                ).build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
