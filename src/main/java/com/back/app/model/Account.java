package com.back.app.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    private Integer account_Id;
    private String username;
    private String account_password;
    private String user_email;
    private String user_first_name;
    private String user_last_name;
    private String user_location;
    private String registration_date;
    
}
