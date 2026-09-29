package com.example.expensetracker.service;

import com.example.expensetracker.dto.UserResponse;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.exception.UserNotFoundException;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Service for user operations
 */
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Create a new user
     */
    public UserResponse createUser(String name) {
        User user = new User(UUID.randomUUID(), Instant.now());
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getUserId(), savedUser.getCreatedAt());
    }
    
    /**
     * Get a user by ID
     */
    @Transactional(readOnly = true)
    public User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
}
