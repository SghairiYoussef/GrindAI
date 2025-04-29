package com.fitnessApp.userservice.service;

import com.fitnessApp.userservice.Repository.UserRepository;
import com.fitnessApp.userservice.dto.RegisterRequestDTO;
import com.fitnessApp.userservice.dto.UserResponseDTO;
import com.fitnessApp.userservice.exception.EmailAlreadyExistsException;
import com.fitnessApp.userservice.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static UserResponseDTO toDto(User savedUser) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        return response;
    }

    public UserResponseDTO register(@Valid RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    public UserResponseDTO getUserProfile(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserService::toDto).orElseThrow();
    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
