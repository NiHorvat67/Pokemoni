package com.back.app.controller;

import java.net.URI;
import java.util.List;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Account;
import com.back.app.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Accounts", description = "Manages user accounts, including retrieval by ID and for the currently authenticated user.")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {

    private final AccountService accountService;

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

    @Operation(summary = "Create a new account", description = "Creates a new account and redirects the user to the /login route, checks if the arguments are valid")
    @PostMapping("/create")
    public ResponseEntity<String> postMethodName(@RequestBody String newAccountString) {

        log.info("Received account: {}", newAccountString);
        try {

            Account newAccount = Account.convertToAccount(newAccountString);
            accountService.registerNewUserAccount(newAccount);
            log.info("Account created successfully via create  for: {}", newAccount.getUserEmail());

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(URI.create("/login"));

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(newAccountString);

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
