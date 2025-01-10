package com.rent_management_system.Configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rent_management_system.Components.JWTAccess;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JWTAccess jwtAccess = new JWTAccess();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String token = jwtAccess.generateToken(user.getName());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        ObjectMapper objectMapper = new ObjectMapper();
//        response.getWriter().write(objectMapper.writeValueAsString(responseData));
        response.sendRedirect("http://localhost:5000?token="+token);
    }
}
