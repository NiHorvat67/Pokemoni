package com.back.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.back.app.service.OAuthRoleService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final OAuthRoleService customOAuth2UserService;

    public SecurityConfig(OAuthRoleService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/advertisment/**", "/", "/login", "/error").permitAll()
                    )
            .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(customOAuth2UserService))
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/error"))
            .exceptionHandling(exception -> exception
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.sendRedirect("/error?denied");
                    }))
            .csrf(csrf -> csrf.disable()); // Disable CSRF protection for OAuth2 development

        return http.build();
    }
}