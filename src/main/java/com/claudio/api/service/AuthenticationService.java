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

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // REGISTRAR: Crear un nuevo usuario y devolver un token JWT
    public AuthResponse register(AuthRequest request) {
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER"); // Asignar rol por defecto

        // Guardar el usuario en la base de datos
        userRepository.save(user);

        // Generamos el token automáticamente para que no tenga que loguearse despues
        var userDetails = new CustomUserDetailsService(userRepository).loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
       return new AuthResponse(jwtToken);
    }

    public AuthResponse login(AuthRequest request) {
        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Si llega aqui, es que se ha autenticado correctamente
        var userDetails = new CustomUserDetailsService(userRepository).loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthResponse(jwtToken);
    }

}
