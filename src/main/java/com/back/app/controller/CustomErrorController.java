package com.back.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class CustomErrorController implements ErrorController {
    
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
                
        String message = "An error occurred.";
        
        return message;
    }
}