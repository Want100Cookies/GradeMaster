package com.datbois.grademaster.controller;

import com.datbois.grademaster.configuration.DomainProperties;
import com.datbois.grademaster.exception.ForbiddenException;
import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.request.EmailVerifyRequest;
import com.datbois.grademaster.request.PasswordChangeRequest;
import com.datbois.grademaster.request.PasswordChangeTokenRequest;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "Request a password change")
    public void requestPasswordChange(@RequestBody PasswordChangeTokenRequest request) {
        User user = userService.findByEmail(request.getEmail());

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
    @ApiOperation(value = "Change password using token")
    public void changePassword(@RequestBody PasswordChangeRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (user.getRetardToken() == null) {
            throw new ForbiddenException();
        }

        if (!user.getRetardToken().equals(request.getToken())) {
            throw new ForbiddenException("Token is not correct");
        }

        user.setPassword(request.getPassword());
        user.setRetardToken(null);

        userService.save(user);
    }

    @RequestMapping(value = "/auth/verify", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Verify email using token")
    public void verifyEmail(@RequestBody EmailVerifyRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (user.getEmailVerifyToken() == null && !user.isVerified()) {
            throw new ForbiddenException("Email is already verified");
        }

        if (!user.getEmailVerifyToken().equals(request.getToken())) {
            throw new ForbiddenException("Invalid token");
        }

        user.setVerified(true);
        user.setEmailVerifyToken(null);

        userService.save(user);
    }
}
