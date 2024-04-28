package com.example.cryptoapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JWTAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JWTAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final String ADMIN_ROLE = "ADMIN";
        final String USER_ROLE = "USER";

        return http
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.PUT, "/user/*/access").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/user/*").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/user/*").authenticated()

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

                        .requestMatchers(HttpMethod.GET, "/wallet").authenticated()
                        .requestMatchers(HttpMethod.POST, "/wallet/transactions").authenticated()
                        .requestMatchers(HttpMethod.GET, "/coins").permitAll()

                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .anyRequest().denyAll()
                ).build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
