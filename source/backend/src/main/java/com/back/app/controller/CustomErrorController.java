package com.back.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController 
public class CustomErrorController implements ErrorController {


@GetMapping("/error")
    public String handleError(@RequestParam(value = "denied", required = false) String denied) {
        
        if (denied != null) {
            // Logic for an access denied (403) error
            return "Access Denied: You do not have permission to view this resource.";
        } else {
            // Logic for general errors (404, 500, etc.)
            return "A general error occurred.";
        }
    }

}