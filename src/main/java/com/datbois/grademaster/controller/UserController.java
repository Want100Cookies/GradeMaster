package com.datbois.grademaster.controller;

import com.datbois.grademaster.configuration.RoleProperties;
import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.RoleService;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleProperties roleProperties;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> users() {
        return userService.findAll();
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        // Set email to unverified and generate random string for the verification email
        user.setVerified(false);
        user.setEmailVerifyToken(UUID.randomUUID().toString());


        // Determine what roles need to be assigned
        Set<Role> roles = new HashSet<>();

        if (userService.count() == 0) {
            roles.add(roleService.findByName("Admin"));
        }

        Pattern studentPattern = Pattern.compile("^([A-Z|a-z|0-9](\\.|_)?)+[A-Z|a-z|0-9]@" + roleProperties.getStudent() + "$");
        Pattern teacherPattern = Pattern.compile("^([A-Z|a-z|0-9](\\.|_)?)+[A-Z|a-z|0-9]@" + roleProperties.getTeacher() + "$");

        if (studentPattern.matcher(user.getEmail()).matches()) {
            roles.add(roleService.findByName("Student"));
        } else if (teacherPattern.matcher(user.getEmail()).matches()) {
            roles.add(roleService.findByName("Teacher"));
        }

        user.setEmail("^([A-Z|a-z|0-9](\\.|_)?)+[A-Z|a-z|0-9]@" + roleProperties.getStudent() + "$");

        user.setRoles(roles);

        return user;
    }
}
