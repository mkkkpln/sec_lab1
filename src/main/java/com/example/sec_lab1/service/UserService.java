package com.example.sec_lab1.service;

import com.example.sec_lab1.model.dto.UserDto;
import com.example.sec_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getEmail(),
                        user.getNickname()
                ))
                .toList();
    }
}
