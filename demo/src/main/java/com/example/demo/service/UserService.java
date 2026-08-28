package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User searchById(Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }
}