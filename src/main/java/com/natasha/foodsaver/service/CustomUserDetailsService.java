package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metod för att ladda användardetaljer baserat på userId
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        // Hämta användaren från repository baserat på userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Returnera en org.springframework.security.core.userdetails.User med användarens email och lösenord
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    // Implementera den obligatoriska loadUserByUsername-metoden från UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Här kan du implementera om du vill använda username-metoden, men just nu används loadUserById
        throw new UnsupportedOperationException("loadUserByUsername is not supported");
    }
}