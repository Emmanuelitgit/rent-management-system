package com.rent_management_system.ServiceImp;

import com.rent_management_system.Repositories.OTPVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OTPVerificationService {

    private final OTPVerificationRepository otpVerificationRepository;
    private JavaMailSender mailSender;

    @Autowired
    public OTPVerificationService(OTPVerificationRepository otpVerificationRepository, JavaMailSender mailSender) {
        this.otpVerificationRepository = otpVerificationRepository;
        this.mailSender = mailSender;
    }

    public boolean sendOTP(String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("OTP Verification");
        message.setText("Hi, kindly enter the opt code below to be verified");
        message.setFrom("eyidana001@gmail.com");
        message.setTo(email);
        mailSender.send(message);
        return true;
    }

    public void removeOTPById(Long id){
        otpVerificationRepository.deleteById(id);
    }
}
