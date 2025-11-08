package com.back.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "oauth2_id", unique = true)
    private String oauth2Id;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "user_last_name")
    private String userLastName;
    
    @Column(name = "user_contact")
    private String userContact;
    
    @Column(name = "user_location")
    private String userLocation;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "account_role")
    private String accountRole;
    @Column(name = "account_rating")
    private Double accountRating;
    
    public Account(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account tempAccount = objectMapper.readValue(jsonString, Account.class);

        
        this.oauth2Id = tempAccount.getOauth2Id();
        this.userEmail = tempAccount.getUserEmail();
        this.userFirstName = tempAccount.getUserFirstName();
        this.userLastName = tempAccount.getUserLastName();
        this.userContact = tempAccount.getUserContact();
        this.userLocation = tempAccount.getUserLocation();
        this.registrationDate = tempAccount.getRegistrationDate();
        this.accountRole = tempAccount.getAccountRole();
    }

    public static Account convertToAccount(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Account.class);
    }

}