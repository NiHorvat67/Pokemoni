package com.back.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Account;
import com.back.app.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Accounts", description = "Manages user accounts, including retrieval by ID and for the currently authenticated user.")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {
    
    private final AccountService accountService;

    @Operation(
        summary = "Retrieve all accounts",
        description = "Returns a comprehensive list of all registered accounts."
    )
    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok().body(accountService.getAllAccounts());
    }


    /*
     * 
     * returns account based on id
     */
    @Operation(
        summary = "Retrieve account by ID",
        description = "Returns the account details for a specific account ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Account> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(accountService.getUserbyId(id));
    }

    @Operation(
        summary = "Retrieve current user account",
        description = "Returns the account details associated with the principal " +
        "of the currently logged-in user (based on OAuth2 ID). Requires TRADER, BUYER, or ADMIN role."
    )
    @Secured({"ROLE_TRADER", "ROLE_BUYER", "ROLE_ADMIN"})
    @GetMapping("/current")
    public Account getCurrentUserAccount(HttpServletRequest request){

        String oauth2Id = request.getUserPrincipal().getName();
        return  accountService.getAccountByOAuth2Id(oauth2Id);

    }
    

}
