package com.bob.projects.twoFactorAuth.controller;


import com.bob.projects.twoFactorAuth.portabls.*;
import com.bob.projects.twoFactorAuth.service.TotpManager;
import com.bob.projects.twoFactorAuth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody LoginInput loginInput) {
        String token = userService.loginUser(loginInput.getUsername(), loginInput.getPassword());
        return ResponseEntity.ok(new AuthenticationResponse(token, StringUtils.isEmpty(token)));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verifyCode(@Valid @RequestBody CodeVerifyInput codeVerifyInput) {
        String token = userService.verify(codeVerifyInput.getUsername(), codeVerifyInput.getCode());
        return ResponseEntity.ok(new AuthenticationResponse(token, StringUtils.isEmpty(token)));
    }

    @PostMapping(value = "/user-creation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@Valid @RequestBody UserInput userInput) {
        String qrCode = userService.userCreation(userInput);
        return ResponseEntity.ok(qrCode);
    }
}
