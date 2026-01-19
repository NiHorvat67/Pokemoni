package com.back.app.controller;

import com.back.app.model.Account;
import com.back.app.service.AccountService;
import com.back.app.service.ImageStorageService;
import com.back.app.service.OAuthRoleService;
import com.back.app.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private OAuthRoleService oAuthRoleService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private ImageStorageService imageStorageService;

    private Account acc;

    @BeforeEach
    void setup() {
        acc = new Account();
        acc.setAccountId(1);
        acc.setUserEmail("test@example.com");
        acc.setUserFirstName("Test");
        acc.setAccountRole("buyer");
        acc.setRegistrationDate(LocalDate.now());
    }

    //api/accounts/
    @Test
    void testGetAllAccounts() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(List.of(acc));

        mockMvc.perform(get("/api/accounts/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userEmail").value("test@example.com"));
    }

    // api/accounts/{id}
    @Test
    void testGetAccountById() throws Exception {
        when(accountService.getUserbyId(1)).thenReturn(acc);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    //api/accounts/create
    @Test
    void testCreateAccount() throws Exception {
        String jsonAccount = """
            {
                "userFirstName": "Test",
                "userLastName": "User",
                "userEmail": "test@example.com",
                "accountRole": "buyer"
            }
        """;

        when(accountService.saveAccount(any())).thenReturn(acc);

        mockMvc.perform(post("/api/accounts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isOk());
    }
}