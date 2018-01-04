package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public List<User> users() {
        return userService.findAll();
    }
}
