package com.securityproject.secureTech.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securityproject.secureTech.Exception.UserAlreadyExistsException;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest.RegisterRequest request) {
        if (request.getEmail().isEmpty() || request.getPassword().isEmpty() || request.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (service.findByEmail(request.getEmail()) == null && service.findByUsername(request.getUsername()) == null) {
            return ResponseEntity.ok(service.register(request));
        } else {
            throw new UserAlreadyExistsException("User is already present in the database");
        }
        
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
