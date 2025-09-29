package com.example.sec_lab1.validation;

import com.example.sec_lab1.model.dto.PostDto;
import com.example.sec_lab1.model.dto.SignInRequest;
import com.example.sec_lab1.model.dto.SignUpRequest;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

@Component
public class XssSanitizer {

    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    private String sanitize(String input) {
        return POLICY.sanitize(input);
    }

    public SignUpRequest sanitize(SignUpRequest signUpRequest) {
        return SignUpRequest.builder()
                .email(sanitize(signUpRequest.getEmail()))
                .password(sanitize(signUpRequest.getPassword()))
                .nickname(sanitize(signUpRequest.getNickname()))
                .build();
    }

    public SignInRequest sanitize(SignInRequest signInRequest) {
        return SignInRequest.builder()
                .email(sanitize(signInRequest.getEmail()))
                .password(sanitize(signInRequest.getPassword()))
                .build();
    }

    public PostDto sanitize(PostDto postDto) {
        return PostDto.builder()
                .content(sanitize(postDto.getContent()))
                .build();
    }

}
