package com.back.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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
    
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "user_email", unique = true) 
    private String userEmail; 
    
    @Column(name = "user_first_name")
    private String userFirstName; 
    
    @Column(name = "user_last_name")
    private String userLastName; 
    
    @Column(name = "user_location")
    private String userLocation; 
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @Column(name = "account_role")   
    private String accountRole; 
    
}