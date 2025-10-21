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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {
    
    private final AccountService accountService;


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok().body(accountService.getAllAccounts());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Account> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(accountService.getUserbyId(id));
    }

    @Secured({"ROLE_TRADER", "ROLE_BUYER", "ROLE_ADMIN"})
    @GetMapping("/current")
    public Account getCurrentUserAccount(HttpServletRequest request){

        String oauth2Id = request.getUserPrincipal().getName();
        return  accountService.getAccountByOAuth2Id(oauth2Id);

    }
    

}
