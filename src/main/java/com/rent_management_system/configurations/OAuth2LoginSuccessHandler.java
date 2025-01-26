package com.rent_management_system.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rent_management_system.authentication.JWTAccess;
import com.rent_management_system.authentication.OTP;
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

    @Autowired
    private JWTAccess jwtAccess;

    /**
     * @auther Emmanuel Yidana
     * @description: an implementation to handle what happens upon successfully login via OAuth2 login
     * @date 016-01-2025
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String token = jwtAccess.generateToken(user.getName());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        ObjectMapper objectMapper = new ObjectMapper();
//        response.getWriter().write(objectMapper.writeValueAsString(responseData));
        response.sendRedirect("http://localhost:3000/homePage?token="+token);
    }
}
