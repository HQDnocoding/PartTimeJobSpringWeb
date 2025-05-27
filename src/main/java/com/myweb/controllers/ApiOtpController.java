/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.services.EmailService;
import java.util.Map;

import com.myweb.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author huaquangdat
 */
@RestController
@RequestMapping("/api")
public class ApiOtpController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private OTPService otpService;

        @PostMapping("/request-otp")
        public ResponseEntity<?> requestOtp(@RequestBody Map<String, String> request) {
            String email = request.get("email");
            String otp = otpService.generateOtp(email);
            String subject = "Mã OTP xác nhận email";
            emailService.sendEmail(email,subject, otp);
            return ResponseEntity.ok("OTP sent to " + email);
        }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }
}
