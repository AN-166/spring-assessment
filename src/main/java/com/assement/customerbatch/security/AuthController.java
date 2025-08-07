package com.assement.customerbatch.security;

import com.assement.customerbatch.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // 1. Authenticate the user
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. Load full UserDetails after authentication
        if (!"admin".equals(username) || !"123456".equals(password)) { // just hardcode for now
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
        }

        // Create a simple UserDetails manually
        UserDetails userDetails = User
            .withUsername(username)
            .password(password)
            .roles("ADMIN") // You can set roles here
            .build();


        // 3. Generate token with user roles
        String token = jwtTokenUtil.generateToken(userDetails);

        // 4. Return the token
        return ResponseEntity.ok(Map.of("token", token));
    }
}
