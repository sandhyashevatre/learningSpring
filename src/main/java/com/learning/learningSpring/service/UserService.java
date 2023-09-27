package com.learning.learningSpring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.learning.learningSpring.entity.User;
import com.learning.learningSpring.repository.UserRepository;

@Service

public class UserService {

    @Autowired

    private UserRepository userRepository;

    public Optional<User> findUserByName(String username) {

        return userRepository.findByName(username);

    }

    public List<User> getAllUsers() {

        return (List<User>) userRepository.findAll();

    }

}
