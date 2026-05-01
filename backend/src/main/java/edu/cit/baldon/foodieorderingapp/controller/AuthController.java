package edu.cit.baldon.foodieorderingapp.controller;

import edu.cit.baldon.foodieorderingapp.dto.*;
import edu.cit.baldon.foodieorderingapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/v1/auth/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody RegisterRequest request) {
        ApiResponse<Map<String, Object>> result = authService.register(request);

        if (!result.isSuccess()) {
            return ResponseEntity.status(result.getStatus() != null ? result.getStatus() : 400).body(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping({"/auth/login", "/v1/auth/login"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        ApiResponse<Map<String, Object>> result = authService.login(request);

        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        return ResponseEntity.ok(result);
    }
}