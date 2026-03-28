package edu.cit.baldon.foodieorderingapp.controller;

import edu.cit.baldon.foodieorderingapp.entity.User;
import edu.cit.baldon.foodieorderingapp.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user){

        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String,String> request){

        return authService.login(
                request.get("email"),
                request.get("password")
        );
    }
}