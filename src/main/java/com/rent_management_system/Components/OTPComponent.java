package com.rent_management_system.Components;

import com.rent_management_system.Authentiication.OTP;
import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.Exception.UnAuthorizedException;
import com.rent_management_system.User.User;
import com.rent_management_system.Authentiication.OTPRepository;
import com.rent_management_system.User.UserRepository;
import com.rent_management_system.Authentiication.OTPService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateAssertionException;
import org.thymeleaf.exceptions.TemplateEngineException;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

@Slf4j
@Component
public class OTPComponent {
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

    public void sendOTP(String email, Long otp, String username) {
        try {
            // Prepare the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("OTP Verification");
            helper.setFrom("eyidana001@gmail.com");
            helper.setTo(email);

            // Prepare the context for Thymeleaf
            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("username", username);

            // Process the Thymeleaf template
            String htmlContent = templateEngine.process("OTPTemplate", context);

            // Set the email content
            helper.setText(htmlContent, true);

            // Send the email
            mailSender.send(message);
        } catch (Exception e) {
            throw new InvalidDataException("Error processing the thymeleaf template");
        }
    }


    // a method to verify user email along with the otp code
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

   // this method is called during login to check if user email already verified or not.
    // access to login is denied if user has not verified yet after sign up.
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


//        helper.setText("""
//                Hi, kindly enter the OTP code below to verify your email
//                The code will expire in 5 minutes.
//                OTP is %s
//                """.formatted(otp));