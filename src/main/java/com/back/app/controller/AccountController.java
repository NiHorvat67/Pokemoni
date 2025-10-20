package com.back.app.controller;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Account;
import com.back.app.service.AccountService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {
    
    private final AccountService userService;

    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllEmployees(){
        return ResponseEntity.ok().body(userService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(userService.getUserbyId(id));
    }

    @GetMapping("/current")
    public Optional<Account> getUsersAccount(Account account){
        return userService.getAccountbyEmail(account.getUserEmail());
    }
    

}
