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
            .requestMatchers("/", "/login", "/error", "/api/advertisements/**", "/api/itemtypes/").permitAll()
            .requestMatchers("/api/accounts/{id}", "/api/accounts/", "/api/accounts/create").permitAll()
            .anyRequest().authenticated())
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService))
            .defaultSuccessUrl("/auth/decide", true)
            .failureUrl("/error?denied"))
        .exceptionHandling(exceptions -> exceptions
            .accessDeniedPage("/error?denied") // The configured URL
        )
        .csrf(csrf -> csrf.disable()); // Disable CSRF protection for OAuth2 development

    return http.build();
  }
}
