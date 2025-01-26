package com.rent_management_system.authentication;

import com.rent_management_system.authentication.OTP;
import com.rent_management_system.authentication.OTPRepository;
import com.rent_management_system.authentication.OTPService;
import com.rent_management_system.exception.InvalidDataException;
import com.rent_management_system.exception.NotFoundException;
import com.rent_management_system.exception.UnAuthorizedException;
import com.rent_management_system.user.User;
import com.rent_management_system.user.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.util.Date;
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