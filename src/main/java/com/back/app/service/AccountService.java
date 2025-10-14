package com.back.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.back.app.repo.AccountRepo;
import com.back.app.model.Account;



@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepo accountRepo;

    public List<Account> getAllEmployees(){
        return accountRepo.findAll();
    }

    public Account getUserbyId(Integer id){

        Optional<Account> optionalEmployee = accountRepo.findById(id);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        log.info("Employee with id: {} doesn't exist", id);
        return null;
    }

}
