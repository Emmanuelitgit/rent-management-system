package com.rent_management_system.Filters;

import com.rent_management_system.Components.JWTAccess;
import com.rent_management_system.Configurations.UserDetailsService;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Exception.UnAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Configuration
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTAccess jwtAccess;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JWTAuthenticationFilter(JWTAccess jwtAccess, UserDetailsService userDetailsService) {
        this.jwtAccess = jwtAccess;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null){
                throw new NotFoundException("No token found");
            }
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtAccess.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtAccess.isTokenValid(token)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        throw new InvalidDataException("Token is invalid or expired.");
                    }
                }
            }
        } catch (UnAuthorizedException | NotFoundException | InvalidDataException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + ex.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}