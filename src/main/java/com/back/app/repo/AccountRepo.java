package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.Account;

public interface AccountRepo extends JpaRepository<Account, Integer>{
    
}
