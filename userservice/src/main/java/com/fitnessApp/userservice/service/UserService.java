package com.fitnessApp.userservice.service;

import com.fitnessApp.userservice.Repository.UserRepository;
import com.fitnessApp.userservice.dto.LoginRequestDTO;
import com.fitnessApp.userservice.dto.RegisterRequestDTO;
import com.fitnessApp.userservice.dto.UserResponseDTO;
import com.fitnessApp.userservice.exception.EmailAlreadyExistsException;
import com.fitnessApp.userservice.exception.UserNotFoundException;
import com.fitnessApp.userservice.model.User;
import com.fitnessApp.userservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
            throw new EmailAlreadyExistsException("User with this email already exists" + request.getEmail());
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

        public Optional<String> authenticate(LoginRequestDTO loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .map(u-> jwtUtil.generateToken(u.getEmail(), String.valueOf(u.getRole())));
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException | SignatureException e) {
            return false;
        }
    }

    public UserResponseDTO getUserProfile(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserService::toDto).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
