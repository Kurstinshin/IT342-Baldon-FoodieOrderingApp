package edu.cit.baldon.foodieorderingapp.service;

import edu.cit.baldon.foodieorderingapp.repository.UserRepository;
import edu.cit.baldon.foodieorderingapp.entity.User;
import edu.cit.baldon.foodieorderingapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
    }

    public ApiResponse<Map<String, Object>> register(RegisterRequest request) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setTimestamp(getTimestamp());

        if (userRepository.existsByEmail(request.getEmail())) {
            response.setSuccess(false);
            response.setStatus(409); // Conflict
            response.setError(new ApiError("DB-002", "Duplicate entry", "Email already exists"));
            return response;
        }

        User user = new User(request.getFirstname(), request.getLastname(), request.getEmail(), encoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        response.setSuccess(true);
        response.setStatus(201);
        response.setMessage("User registered successfully");
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", savedUser.getId());
        data.put("email", savedUser.getEmail());
        data.put("firstName", savedUser.getFirstname());
        data.put("lastName", savedUser.getLastname());
        data.put("role", savedUser.getRole());
        data.put("accessToken", UUID.randomUUID().toString()); // Mock token
        response.setData(data);

        return response;
    }

    public ApiResponse<Map<String, Object>> login(LoginRequest request) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setTimestamp(getTimestamp());

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
            response.setSuccess(false);
            response.setError(new ApiError("AUTH-001", "Invalid credentials", "Email or password is incorrect"));
            return response;
        }

        response.setSuccess(true);
        
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("email", user.getEmail());
        userData.put("firstname", user.getFirstname());
        userData.put("lastname", user.getLastname());
        userData.put("role", user.getRole());
        
        data.put("user", userData);
        data.put("accessToken", UUID.randomUUID().toString());
        data.put("refreshToken", UUID.randomUUID().toString());
        
        response.setData(data);

        return response;
    }
}