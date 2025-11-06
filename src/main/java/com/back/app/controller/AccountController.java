package com.back.app.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.back.app.model.Account;
import com.back.app.service.AccountService;
import com.back.app.service.OAuthRoleService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonParseException;

@Tag(name = "Accounts", description = "Manages user accounts, including retrieval by ID and for the currently authenticated user.")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Operation(summary = "Retrieve all accounts", description = "Returns a comprehensive list of all registered accounts.")
    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok().body(accountService.getAllAccounts());
    }

    @Operation(summary = "Retrieve account by ID", description = "Returns the account details for a specific account ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(accountService.getUserbyId(id));
    }

    @Operation(summary = "Retrieve current user account", description = "Returns the account details associated with the principal "
            +
            "of the currently logged-in user (based on OAuth2 ID). Requires TRADER, BUYER, or ADMIN role.")
    @Secured({ "ROLE_TRADER", "ROLE_BUYER", "ROLE_ADMIN" })
    @GetMapping("/current")
    public Account getCurrentUserAccount(HttpServletRequest request) {

        String oauth2Id = request.getUserPrincipal().getName();
        return accountService.getAccountByOAuth2Id(oauth2Id);

    }

    @GetMapping("/current_id")
    public Integer getCurrentUserId(HttpServletRequest request) {

        String oauth2Id = request.getUserPrincipal().getName();
        return accountService.getAccountByOAuth2Id(oauth2Id).getAccountId();

    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(
            @RequestBody String newAccountString,
            @AuthenticationPrincipal OAuth2User oauth2User) {

        try {
           
            String accessToken = getCurrentUserAccessToken();

            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("No access token available");
            }

            
            String userEmail = fetchEmailWithAccessToken(oauth2User, accessToken);

            if (userEmail == null) {
                return ResponseEntity.badRequest()
                        .body("Could not determine user email from OAuth2 provider");
            }

            String oauth2Id = oauth2User.getName();

            log.info("Extracted - OAuth2 ID: {}, Email: {}", oauth2Id, userEmail);

            Account newAccount = Account.convertToAccount(newAccountString);
            newAccount.setOauth2Id(oauth2Id);
            newAccount.setUserEmail(userEmail);

         
            if (newAccount.getRegistrationDate() == null) {
                newAccount.setRegistrationDate(LocalDate.now());
            }
            if (newAccount.getAccountRole() == null || newAccount.getAccountRole().trim().isEmpty()) {
                newAccount.setAccountRole("buyer");
            }

            accountService.saveAccount(newAccount);
            log.info("Account created successfully for: {}", userEmail);

            return ResponseEntity.ok().body("Account created for: " + userEmail);

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

 
    private String getCurrentUserAccessToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

                
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                        authToken.getAuthorizedClientRegistrationId(),
                        authToken.getName());

                if (client != null && client.getAccessToken() != null) {
                    return client.getAccessToken().getTokenValue();
                }
            }
        } catch (Exception e) {
            log.error("Error getting access token: {}", e.getMessage());
        }

        return null;
    }

 
    private String fetchEmailWithAccessToken(OAuth2User oAuth2User, String accessToken) {
        
        String email = (String) oAuth2User.getAttributes().get("email");
        if (email != null) {
            return email;
        }

    
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Accept", "application/vnd.github.v3+json");

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<Map[]> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    Map[].class);

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
