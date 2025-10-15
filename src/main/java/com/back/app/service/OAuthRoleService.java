package com.back.app.service;

import com.back.app.model.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthRoleService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    private final AccountService accountService;

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
 

        String email = oAuth2User.getAttribute("email");
        
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        log.info(attributes.toString());

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
                    // Account exists but has no role assigned
                    authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
                }
            } else {
                // email is not present in db
                authorities.add(new SimpleGrantedAuthority("ROLE_GUEST")); 
                log.info("load_user : User is not present in db");
            }
        } else {
            // email is null
            log.info("load_user : email is null");
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        }

        String userNameAttributeName = userRequest.getClientRegistration()
                                                  .getProviderDetails()
                                                  .getUserInfoEndpoint()
                                                  .getUserNameAttributeName();
                                                  
        return new DefaultOAuth2User(authorities, 
                                    oAuth2User.getAttributes(), 
                                    userNameAttributeName);
    }
}
