package com.claudio.api.security.config;

import com.claudio.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
private final JwtService jwtService;
private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
            ) throws ServletException, IOException {
     // Busco el header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // Si no hay header o no empieza con "Bearer ", sigo con el filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Extraigo el token JWT del header
        jwt = authHeader.substring(7);
        // Extraigo el usuario (email) del token
        userEmail = jwtService.extractUsername(jwt);
        // Si hay un usuario y no está autenticado sistema
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Busco el usuario en mi base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            // Si el token es válido, autentico al usuario
            if(jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Registramos al usuario como autenticado en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            filterChain.doFilter(request, response);
        }


    }
}
