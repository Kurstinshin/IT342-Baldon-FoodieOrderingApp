package edu.cit.baldon.foodieorderingapp.feature.auth;

import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Auth Feature — Controller
 * Routes: POST /api/v1/auth/register, POST /api/v1/auth/login
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody RegisterRequest request) {
        ApiResponse<Map<String, Object>> result = authService.register(request);
        int statusCode = result.getStatus() != null ? result.getStatus() : (result.isSuccess() ? 201 : 400);
        return ResponseEntity.status(statusCode).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        ApiResponse<Map<String, Object>> result = authService.login(request);
        return result.isSuccess()
            ? ResponseEntity.ok(result)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
