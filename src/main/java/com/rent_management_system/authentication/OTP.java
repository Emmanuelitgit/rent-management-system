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

    @Component
    public static class JWTAccess {

        String SECRET = "RKUGLRKBKBSKLGSFIJSBKFBKJSDJBVugdtyidvctyfktvgkuyrcggchvrydtxtxuvyvgghghhhjhkjkjjurtyvkgvK";
        long MINUTES = TimeUnit.MINUTES.toMillis(30);

        /**
         * @auther Emmanuel Yidana
         * @description: A method to generate a jwt token
         * @date 016-01-2025
         * @param: username
         * @return token
         */
        public String generateToken(String username){
            Map<String, Object> claims = new HashMap<>();
            claims.put("issuer", "www.emma.com");
            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(secretKey())
                    .setExpiration(Date.from(Instant.now().plusMillis(MINUTES)))
                    .setIssuedAt(Date.from(Instant.now()))
                    .setSubject(username)
                    .compact();
        }

        /**
         * @auther Emmanuel Yidana
         * @description: A method to extract user details or claims from a token
         * @date 016-01-2025
         * @param: token
         * @throws UnAuthorizedException - throws UnAuthorizedException if token is invalid
         * @return claims such as email, username,authorities etc
         */
        public Claims getClaims(String token) {
           try{
               return Jwts.parserBuilder()
                       .setSigningKey(secretKey())
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
           } catch (SignatureException | ExpiredJwtException e) {
               throw new UnAuthorizedException("Invalid token or signature");
           }
        }

        /**
         * @auther Emmanuel Yidana
         * @description: A method to extract username from a token
         * @date 016-01-2025
         * @param: token
         * @return username
         */
        public String extractUsername(String token){
            Claims claims = getClaims(token);
            return claims.getSubject();
        }

        /**
         * @auther Emmanuel Yidana
         * @description: A method to check if token is valid
         * @date 016-01-2025
         * @param: token
         * @return boolean
         */
        public boolean  isTokenValid(String token){
            Claims claims = getClaims(token);
            return Date.from(Instant.now()).before(claims.getExpiration());
        }

        /**
         * @auther Emmanuel Yidana
         * @description: A method to generate a secret key for token generation and verification
         * @date 016-01-2025
         * @return SecretKey
         */
        private SecretKey secretKey(){
            byte[] decodedKey = Base64.getDecoder().decode(SECRET);
            return Keys.hmacShaKeyFor(decodedKey);
        }
    }

    @Slf4j
    @Component
    public static class OTPComponent {
        private final OTPRepository otpRepository;
        private final UserRepository userRepository;
        private final OTPService otpVerificationService;
        private final JavaMailSender mailSender;
        private final TemplateEngine templateEngine;

        @Autowired
        public OTPComponent(OTPRepository otpRepository, UserRepository userRepository, OTPService otpVerificationService, JavaMailSender mailSender, TemplateEngine templateEngine) {
            this.otpRepository = otpRepository;
            this.userRepository = userRepository;
            this.otpVerificationService = otpVerificationService;
            this.mailSender = mailSender;
            this.templateEngine = templateEngine;
        }

        // a method to genera a random otp code
        public Long generateOTP(){
            RandomGenerator generator = new Random();
            return generator.nextLong(2001, 9000);
        }

        /**
         * @auther Emmanuel Yidana
         * @description: A method to send an otp via email
         * @date 016-01-2025
         * @param: email, otp, username
         * @throws InvalidDataException - throws InvalidDataException if any kind of exception occurred
         */
        public void sendOTP(String email, Long otp, String username) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setSubject("OTP Verification");
                helper.setFrom("eyidana001@gmail.com");
                helper.setTo(email);

                Context context = new Context();
                context.setVariable("otp", otp);
                context.setVariable("username", username);

                String htmlContent = templateEngine.process("OTPTemplate", context);
                helper.setText(htmlContent, true);
                mailSender.send(message);
            } catch (Exception e) {
                throw new InvalidDataException("Error processing the thymeleaf template");
            }
        }

        /**
         * @auther Emmanuel Yidana
         * @description: A method to verify otp against email
         * @date 016-01-2025
         * @param: email, otp
         * @throws NotFoundException - if provided email does not exist or otp does not exist
         * @throws UnAuthorizedException - if otp does not match or is expired
         */
        public void verifyOtp(String email, Long otp){
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                throw new NotFoundException("No user associated with the provided email");
            }
            User user = userOptional.get();
            Optional<OTP> verificationOptional = otpRepository.findOTPVerificationByUser_Id(user.getId());
            if (verificationOptional.isPresent()){
                boolean expirationTime = Date.from(Instant.now()).before(verificationOptional.get().getCreatedAt());
                int isOtpMatch = verificationOptional.get().getOtp().compareTo(otp);
                if (!expirationTime){
                    throw new UnAuthorizedException("Otp has expired");
                }
                if (isOtpMatch != 0){
                    throw new UnAuthorizedException("OTP code not matched");
                }
                otpVerificationService.removeOTPById(verificationOptional.get().getId());
                return;
            }
            throw new NotFoundException("No OTP code associated with the provided email!");
        }

        /**
         * @auther Emmanuel Yidana
         * @description: this method is invoked during login to check if user email is verified or not
         * @date 016-01-2025
         * @param: email
         * @throws NotFoundException - if provided email does not exist
         * @throws UnAuthorizedException - if email is not verified
         */
        public void verifyUserOTPStatusDuringLogin(String email){
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                throw new NotFoundException("User data not found");
            }
            User user = userOptional.get();
            Optional<OTP> verificationOptional = otpRepository.findOTPVerificationByUser_Id(user.getId());
            if (verificationOptional.isPresent()){
                throw new UnAuthorizedException("User not verified");
            }
        }
    }
}
