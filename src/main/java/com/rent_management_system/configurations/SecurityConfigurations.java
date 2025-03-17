package com.rent_management_system.configurations;

import com.rent_management_system.filters.JWTAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityConfigurations {

    private final UserDetailsService userDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfiguration corsConfiguration;

    @Autowired
    public SecurityConfigurations(UserDetailsService userDetailsService, JWTAuthenticationFilter jwtAuthenticationFilter, CorsConfiguration corsConfiguration) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfiguration = corsConfiguration;
    }


    // swagger endpoints configurations
    private static final String[] SWAGGER_ENDPOINTS = {
            "/", HttpMethod.GET.name(),
            "/actuator/**",
            "/swagger-ui/**",
            "/configuration/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/webjars/**",
            "/assets/**",
            "/static/**",
    };

    /**
     * @auther Emmanuel Yidana
     * @description: A bean to handle security configurations and operations
     * @date 016-01-2025
     * @param: httpSecurity
     * @return securityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfiguration))
                .authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                            .anyRequest().permitAll();
                })
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer
                            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                            .accessDeniedHandler(new AccessDeniedHandler());
                })
                .oauth2Login(oauth -> oauth
                        .successHandler(new OAuth2LoginSuccessHandler())
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A bean to handle authentication manager configuration for authenticating and loading user details by calling on the loadUserByUsername from the UserDetailsService class
     * @date 016-01-2025
     * @return ProviderManager
     */
    @Bean
    AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A bean to handle password encoding and decoding
     * @date 016-01-2025
     * @return encoded or decoded password
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAwareImpl auditorAware(){
        return new AuditorAwareImpl();
    }

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}