package com.back.app;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.back.app.model.Account;
import com.back.app.repo.AccountRepo;
import com.back.app.service.AccountService;

public class AccountServiceTest {
    
    private AccountRepo accountRepo;
    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountRepo = Mockito.mock(AccountRepo.class);
        accountService = new AccountService(accountRepo);
    }

    // uspjesna registracija 
    @Test
    void testRegisterUser_RegularCase() {
        Account acc = new Account();
        acc.setUserEmail("test@example.com");
        acc.setAccountRole("buyer");
        acc.setRegistrationDate(LocalDate.now());

        when(accountRepo.findByUserEmail("test@example.com"))
                .thenReturn(Optional.empty());
        when(accountRepo.save(acc)).thenReturn(acc);

        Account result = accountService.registerNewUserAccount(acc);

        assertNotNull(result);
        assertEquals("buyer", result.getAccountRole());
        verify(accountRepo).save(acc);
    }

    // rubni slucaj: datum registracije je null
    @Test
    void testRegisterUser_NullRegistrationDate() {
        Account acc = new Account();
        acc.setUserEmail("edge@example.com");
        acc.setAccountRole("buyer");
        acc.setRegistrationDate(null);

        when(accountRepo.findByUserEmail("edge@example.com"))
                .thenReturn(Optional.empty());
        when(accountRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = accountService.registerNewUserAccount(acc);

        assertNotNull(result.getRegistrationDate());
    }

    // izazivanje pogreske - role ne postoji
    @Test
    void testRegisterUser_InvalidRole_ThrowsException() {
        Account acc = new Account();
        acc.setUserEmail("wrongrole@example.com");
        acc.setAccountRole("invalidRole");

        assertThrows(IllegalArgumentException.class, () -> {
            accountService.registerNewUserAccount(acc);
        });
    }

   // poziv nepostojece funkcionalnosti
    @Test
    void testNonExistingFunctionality() {
        when(accountRepo.findById(any())).thenThrow(new UnsupportedOperationException());

        assertThrows(UnsupportedOperationException.class, () -> {
            accountRepo.findById(999);
        });
    }
}



