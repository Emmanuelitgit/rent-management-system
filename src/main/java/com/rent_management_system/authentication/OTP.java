package com.rent_management_system.authentication;

import com.rent_management_system.exception.InvalidDataException;
import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.exception.UnAuthorizedException;
import com.rent_management_system.user.User;
import com.rent_management_system.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp_tb")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column
    public Long otp;
    @Column
    public Date createdAt;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    public boolean status;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
