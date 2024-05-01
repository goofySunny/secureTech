package com.securityproject.secureTech.Auth;


import com.securityproject.secureTech.config.JwtService;
import com.securityproject.secureTech.user.Role;
import com.securityproject.secureTech.user.User;
import com.securityproject.secureTech.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Todo: check the register request for null register request
    @SuppressWarnings("null")
        public AuthenticationResponse register(AuthenticationRequest.RegisterRequest request) {
                var user = User.builder()
                        .name(request.getName())
                        .userName(request.getUsername())
                        .email(request.getEmail())
                        .pass(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .build();
                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
        }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
