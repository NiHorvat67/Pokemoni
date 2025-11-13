package com.back.app.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4jpublic 
class HomeController {
    
    @GetMapping("/")
    String rootRoute(){
        return "Hello from /";
    }

    @GetMapping("/home")
    String homeRoute(){
        return "Hello from home";
    }

}
