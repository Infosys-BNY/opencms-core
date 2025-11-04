package org.opencms.user.controller;

import org.opencms.user.dto.LoginRequest;
import org.opencms.user.dto.LoginResponse;
import org.opencms.user.entity.User;
import org.opencms.user.exception.AuthenticationException;
import org.opencms.user.security.JwtTokenProvider;
import org.opencms.user.service.AuthenticationService;
import org.opencms.user.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private SessionService sessionService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authenticationService.authenticateUser(
                request.getUsername(), 
                request.getPassword(),
                request.getSecondFactorCode()
            );
            
            String token = tokenProvider.generateToken(user.getUserId(), user.getUserName());
            String sessionId = sessionService.createSession(
                user.getUserId(), 
                user.getUserName(),
                null,
                user.getUserOu()
            );
            
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setSessionId(sessionId);
            response.setUsername(user.getUserName());
            response.setUserId(user.getUserId());
            
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Session-Id") String sessionId) {
        sessionService.invalidateSession(sessionId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyToken() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "valid");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            if (tokenProvider.validateToken(token)) {
                String userId = tokenProvider.getUserIdFromToken(token);
                
                Map<String, String> response = new HashMap<>();
                response.put("status", "valid");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
