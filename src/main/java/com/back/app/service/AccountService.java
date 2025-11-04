package com.back.app.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.UserDefinedObjectType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.back.app.repo.AccountRepo;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import com.back.app.model.Account;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepo accountRepo;

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Account getUserbyId(Integer id) {

        Optional<Account> optionalAccount = accountRepo.findById(id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        log.info("Employee with id: {} doesn't exist", id);
        return null;
    }

    public Optional<Account> getAccountbyEmail(String email) {
        if (email == null) {
            log.warn("Attempted to search for an account with a null email. Returning empty.");
            return Optional.empty();
        }

        List<Account> accounts = this.getAllAccounts();

        Optional<Account> ret = accounts.stream()
                .filter(a -> {
                    String accountEmail = a.getUserEmail();

                    if (accountEmail == null) {
                        log.info("Account Email is null, skipping comparison.");
                        return false;
                    }

                    // The input 'email' is guaranteed non-null here.
                    boolean isEqual = email.equals(accountEmail.trim());

                    // log.info("Comparing stored email '{}' with search email '{}'",
                    // accountEmail.trim(), email);
                    // log.info("equals = {}", isEqual);

                    return isEqual;
                })
                .findFirst();

        if (ret.isPresent()) {
            log.info("Found account: {}", ret.get().toString());
        } else {
            log.info("Account not found for search email: {}", email);
        }

        return ret;
    }

    public Account getAccountByOAuth2Id(String oauth2Id) {
        Optional<Account> account = accountRepo.findByOauth2Id(oauth2Id);
        if (!account.isPresent()) {
            log.warn("oauth2Id not presend in db");
            return null;
        }
        return account.get();
    }

    public Account saveAccount(Account account) {
        return accountRepo.save(account);
    }

    public Account registerNewUserAccount(Account account) throws IllegalArgumentException {

        if (!account.getAccountRole().equals("buyer")
                && !account.getAccountRole().equals("trader") && !account.getAccountRole().equals("admin")) {
            throw new IllegalArgumentException("Invalid role selection");
        }

        if (accountRepo.findByUserEmail(account.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("Email address already in use");
        }

 

        if (account.getRegistrationDate() == null) {
            account.setRegistrationDate(LocalDate.now());
        }

        return accountRepo.save(account);
    }

}
