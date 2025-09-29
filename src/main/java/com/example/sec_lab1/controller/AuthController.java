package com.example.sec_lab1.controller;

import com.example.sec_lab1.model.dto.JwtAuthenticationResponse;
import com.example.sec_lab1.model.dto.SignInRequest;
import com.example.sec_lab1.model.dto.SignUpRequest;
import com.example.sec_lab1.service.AuthenticationService;
import com.example.sec_lab1.validation.XssSanitizer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    private final XssSanitizer xssSanitizer;


    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        request = xssSanitizer.sanitize(request);
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        request = xssSanitizer.sanitize(request);
        return authenticationService.signIn(request);
    }
}
