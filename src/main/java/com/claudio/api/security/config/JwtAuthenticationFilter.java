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

        System.out.println("---INICIO FILTRO JWT---");

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(" No hay header Authorization o no empieza con Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Header encontrado: " + authHeader);
        final String jwt = authHeader.substring(7);
        System.out.println("Token extraído: " + jwt);

        final String userEmail = jwtService.extractUsername(jwt);
        System.out.println("Usuario en Token: " + userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("🗄️ Usuario encontrado en BD: " + userDetails.getUsername());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("El Token es VÁLIDO");

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Usuario AUTENTICADO en el Contexto de Seguridad");
            } else {
                System.out.println("El Token NO es válido");
            }
        }

        System.out.println("--- FIN FILTRO JWT (Pasando al siguiente) ---");
        filterChain.doFilter(request, response);
    }
}
