package com.example.sec_lab1.service;

import com.example.sec_lab1.exception.ApplicationException;
import com.example.sec_lab1.model.User;
import com.example.sec_lab1.model.dto.JwtAuthenticationResponse;
import com.example.sec_lab1.model.dto.SignInRequest;
import com.example.sec_lab1.model.dto.SignUpRequest;
import com.example.sec_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new ApplicationException("User with such email already exists",
                    ApplicationException.Reason.ALREADY_EXISTS);
        }

        var user = User.builder()
                .id(0)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new ApplicationException("Bad request", ApplicationException.Reason.BAD_REQUEST);
        }

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userRepository.findByEmail(request.getEmail());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

}
