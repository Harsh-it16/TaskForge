package com.example.todo_api.service;

import com.example.todo_api.dto.LoginRequest;
import com.example.todo_api.dto.RegisterRequest;
import com.example.todo_api.entity.User;
import com.example.todo_api.repository.UserRepository;
import com.example.todo_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest req){
        if(userRepo.existsByUsername(req.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepo.save(user);
        return "User registered successfully";
    }

    public String login(LoginRequest req){
        //Throws BadCredentilsException if wrong password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        return jwtUtil.generateToken(req.getUsername());
    }
}
