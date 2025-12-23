package com.claudio.api.service;


import com.claudio.api.repository.UserRepository;
import com.claudio.api.model.User;
import com.claudio.api.dto.AuthRequest;
import com.claudio.api.dto.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final CustomUserDetailsService customUserDetailsService;

        public AuthenticationService(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager,
                                     CustomUserDetailsService customUserDetailsService) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
            this.authenticationManager = authenticationManager;
            this.customUserDetailsService = customUserDetailsService;
        }

        public AuthResponse register(AuthRequest request) {
            var user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole("USER");
            userRepository.save(user);
            // Aqui uso la viriable inyectada
            var userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            String jwtToken = jwtService.generateToken(userDetails);
            return new AuthResponse(jwtToken);
        }

        public AuthResponse login(AuthRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            // USAMOS LA VARIABLE INYECTADA (CORRECTO)
            var userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
            String jwtToken = jwtService.generateToken(userDetails);
            return new AuthResponse(jwtToken);
        }
    }

