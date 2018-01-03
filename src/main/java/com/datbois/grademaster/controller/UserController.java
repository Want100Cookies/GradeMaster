package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/api")
    public List<User> users() {
        return userService.findAll();
    }
}
