package edu.cit.baldon.foodieorderingapp.service;

import edu.cit.baldon.foodieorderingapp.repository.UserRepository;
import edu.cit.baldon.foodieorderingapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String register(User user){

        if(userRepository.existsByEmail(user.getEmail())){
            return "Email already exists";
        }

        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(String email,String password){

        User user = userRepository.findByEmail(email).orElse(null);

        if(user==null){
            return "User not found";
        }

        if(encoder.matches(password,user.getPassword())){
            return "Login successful";
        }

        return "Invalid password";
    }
}