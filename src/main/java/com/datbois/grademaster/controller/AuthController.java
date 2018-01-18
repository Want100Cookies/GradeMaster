package com.datbois.grademaster.controller;

import com.datbois.grademaster.configuration.DomainProperties;
import com.datbois.grademaster.exception.ForbiddenException;
import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DomainProperties domainProperties;

    @RequestMapping(value = "/auth/retard", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void requestPasswordChange(@RequestBody Map<String, String> request) {
        User user = userService.findByEmail(request.get("email"));

        if (user.getRetardToken() == null) {
            user.setRetardToken(UUID.randomUUID().toString());
            userService.save(user);
        }

        Email verificationMail = new Email(
                user.getEmail(),
                "Reset your password",
                "Someone has requested a password change for your email. You can click on the following button to reset it. If you did not request a password change you can ignore this email.",
                domainProperties.getBase() + String.format("/#!/reset?token=%s", user.getRetardToken()),
                "Reset"
        );

        emailService.sendToEmailQueue(verificationMail);
    }

    @RequestMapping(value = "/auth/retard", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changePassword(@RequestBody Map<String, String> request) {
        User user = userService.findByEmail(request.get("email"));

        if (user.getRetardToken() == null) {
            throw new ForbiddenException();
        }

        if (!user.getRetardToken().equals(request.get("token"))) {
            throw new ForbiddenException("Token is not correct");
        }

        user.setPassword(request.get("password"));
        user.setRetardToken(null);

        userService.save(user);

        // Send notification of changed password
    }

    @RequestMapping(value = "/auth/verify", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void verifyEmail(@RequestBody Map<String, String> request) {
        User user = userService.findByEmail(request.get("email"));

        if (user.getEmailVerifyToken() == null && !user.isVerified()) {
            throw new ForbiddenException("Email is already verified");
        }

        if (!user.getEmailVerifyToken().equals(request.get("token"))) {
            throw new ForbiddenException("Invalid token");
        }

        user.setVerified(true);
        user.setEmailVerifyToken(null);

        userService.save(user);
    }
}
