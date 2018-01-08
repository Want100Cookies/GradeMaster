package com.datbois.grademaster.controller;

import com.datbois.grademaster.exception.ForbiddenException;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PasswordController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/auth/retard", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void requestPasswordChange(@RequestBody Map<String, String> request) {
        User user = userService.findByEmail(request.get("email"));

        if (user.getRetardToken() == null) {
            user.setRetardToken(UUID.randomUUID().toString());
            userService.save(user);
        }

        // Send email
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
}
