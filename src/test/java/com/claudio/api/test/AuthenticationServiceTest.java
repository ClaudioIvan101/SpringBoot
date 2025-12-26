package com.claudio.api.test;

import com.claudio.api.dto.AuthRequest;
import com.claudio.api.dto.AuthResponse;
import com.claudio.api.model.User;
import com.claudio.api.repository.UserRepository;
import com.claudio.api.service.AuthenticationService;
import com.claudio.api.service.CustomUserDetailsService;
import com.claudio.api.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthenticationService authenticationService;

    // Test para verificar que el Registro guarde el usuario y devuelva un token JWT
    @Test
    void register_ShouldSaveUserAndReturnJwtToken() {
        AuthRequest request = new AuthRequest("UserTest", "1234");
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(jwtService.generateToken(any())).thenReturn("jwtToken");
        // Mockeamos el servicio de detalles de usuario
        when(customUserDetailsService.loadUserByUsername(any()))
                .thenReturn(new org.springframework.security.core.userdetails.User
                        ("UserTest", "encodedPass",
                                java.util.Collections.emptyList()));
        AuthResponse response = authenticationService.register(request);

        // Verificamos que el token JWT devuelto sea el esperado
        assertNotNull(response.getToken());
        verify(userRepository).save(any(User.class));
    }
    // Verificar Login Exitoso
    @Test
    void login_ShouldAuthenticateAndReturnToken() {
        AuthRequest request = new AuthRequest("user", "pass");
        when(jwtService.generateToken(any())).thenReturn("token-login");
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new org.springframework.security.core.userdetails.User("user", "pass", java.util.Collections.emptyList()));

        AuthResponse response = authenticationService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        // Verifica que se llamó al manager de autenticación real
        verify(authenticationManager).authenticate(any());
    }
}
