package com.back.app.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.back.app.model.Account;

public interface AccountRepo extends JpaRepository<Account, Integer>{

    Optional<Account> findByUserEmail(@Param("userEmail") String user_email); 

    Optional<Account> findByUsername(@Param("username") String username); 

    Optional<Account> findByOauth2Id(String oauth2Id);
    
}
