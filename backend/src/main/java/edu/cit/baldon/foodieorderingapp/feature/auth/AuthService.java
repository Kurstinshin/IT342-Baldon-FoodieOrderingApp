package edu.cit.baldon.foodieorderingapp.feature.auth;

import edu.cit.baldon.foodieorderingapp.shared.ApiError;
import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import edu.cit.baldon.foodieorderingapp.shared.SliceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Auth Feature — Service
 * Handles all authentication business logic (register & login).
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ApiResponse<Map<String, Object>> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            ApiResponse<Map<String, Object>> r = ApiResponse.fail(
                "Email already registered",
                new ApiError("DB-002", "Duplicate entry", "Email already exists"),
                SliceUtils.timestamp()
            );
            r.setStatus(409);
            return r;
        }

        User user = new User(
            request.getFirstname(), request.getLastname(),
            request.getEmail(), encoder.encode(request.getPassword())
        );
        User saved = userRepository.save(user);

        Map<String, Object> data = new HashMap<>();
        data.put("userId",      saved.getId());
        data.put("email",       saved.getEmail());
        data.put("firstName",   saved.getFirstname());
        data.put("lastName",    saved.getLastname());
        data.put("role",        saved.getRole());
        data.put("username",    saved.getFirstname() + " " + saved.getLastname());
        data.put("token",       UUID.randomUUID().toString());

        ApiResponse<Map<String, Object>> r = SliceUtils.ok(data, "User registered successfully");
        r.setStatus(201);
        return r;
    }

    public ApiResponse<Map<String, Object>> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.fail(
                "Invalid credentials",
                new ApiError("AUTH-001", "Unauthorized", "Email or password is incorrect"),
                SliceUtils.timestamp()
            );
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId",   user.getId());
        data.put("username", user.getFirstname() + " " + user.getLastname());
        data.put("role",     user.getRole());
        data.put("token",    UUID.randomUUID().toString());

        return SliceUtils.ok(data, "Login successful");
    }
}
