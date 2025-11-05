package com.back.app.controller;

import org.hibernate.Internal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import com.back.app.model.Account;
import com.back.app.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequiredArgsConstructor
@Validated
@Slf4j
public class RedirectController {

    @Value("${app.frontend.url}")
    private String frontendBaseUrl;
    
    private final AccountService accountService;

    @GetMapping("/auth/decide")
    public String getMethodName(HttpServletRequest request) {
        

        String oauth2Id = request.getUserPrincipal().getName();
        Account ret = accountService.getAccountByOAuth2Id(oauth2Id);

        log.info(frontendBaseUrl);
        if(ret == null){
            log.info("redirect:" + frontendBaseUrl + "/auth/2");
            return "redirect:" + frontendBaseUrl + "/auth/2";

        }else{
            log.info("redirect:" + frontendBaseUrl + "/auth/2");
            return "redirect:" + frontendBaseUrl + "/";

        }

    }
    



}
