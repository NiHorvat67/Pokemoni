package com.back.app.service;

import com.back.app.model.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthRoleService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountService accountService;
    private final RestTemplate restTemplate = new RestTemplate();

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        
        String email = extractEmail(oAuth2User, userRequest, registrationId);
        
        log.info("Email extracted: {}", email);
        log.info("All attributes: {}", attributes);

        Set<GrantedAuthority> authorities = new HashSet<>(oAuth2User.getAuthorities());

        if (email != null) {
            Optional<Account> accountOpt = accountService.getAccountbyEmail(email);
            log.info("load_user : email received {}", email);
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                String role = account.getAccountRole();
                log.info("load_user : Account: {} has role {}", account, role);
                if (role != null && !role.trim().isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
                }
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
                log.info("load_user : User is not present in db");
            }
        } else {
            log.info("load_user : email is null");
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        }

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
    }

    private String extractEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest, String registrationId) {
        
        if ("github".equals(registrationId)) {
            return extractGitHubEmail(oAuth2User, userRequest);
        }
        
        
        return (String) oAuth2User.getAttributes().get("email");
    }

    private String extractGitHubEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        
        String email = (String) oAuth2User.getAttributes().get("email");
        if (email != null) {
            return email;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + userRequest.getAccessToken().getTokenValue());
            
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            
            ResponseEntity<Map[]> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                Map[].class
            );
            
            if (response.getBody() != null) {
                
                for (Map<String, Object> emailData : response.getBody()) {
                    Boolean primary = (Boolean) emailData.get("primary");
                    Boolean verified = (Boolean) emailData.get("verified");
                    String emailAddress = (String) emailData.get("email");
                    
                    if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified) && emailAddress != null) {
                        return emailAddress;
                    }
                }
                
                for (Map<String, Object> emailData : response.getBody()) {
                    Boolean verified = (Boolean) emailData.get("verified");
                    String emailAddress = (String) emailData.get("email");
                    
                    if (Boolean.TRUE.equals(verified) && emailAddress != null) {
                        return emailAddress;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch GitHub user emails", e);
        }
        
        return null;
    }
}