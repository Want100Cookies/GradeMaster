package com.datbois.grademaster.controller;

import com.datbois.grademaster.configuration.RoleProperties;
import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.RoleService;
import com.datbois.grademaster.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public List<User> users() {
        return userService.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isCurrentUser(#userId)")
    @ResponseStatus(HttpStatus.OK)
    public User user(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Set email to unverified and generate random string for the verification email
        user.setVerified(false);
        user.setEmailVerifyToken(UUID.randomUUID().toString());

        // Determine what roles need to be assigned
        Set<Role> roles = new HashSet<>();

        if (userService.count() == 0) {
            roles.add(roleService.findByName("Admin"));
        }

        Pattern pattern = Pattern.compile(String.format(
                "([A-Z|a-z|0-9](\\.|_)?)+[A-Z|a-z|0-9]\\@(%s|%s)",
                roleProperties.getStudent().replace(".", "\\."),
                roleProperties.getTeacher().replace(".", "\\.")
        ));

        Matcher matcher = pattern.matcher(user.getEmail());

        if (matcher.find()) {
            if (matcher.group(3).equals(roleProperties.getStudent())) {
                roles.add(roleService.findByName("Student"));
            }

            if (matcher.group(3).equals(roleProperties.getTeacher())) {
                roles.add(roleService.findByName("Teacher"));
            }
        } else {
            return new ResponseEntity<>("Email address not accepted", HttpStatus.BAD_REQUEST);
        }

        user.setRoles(roles);

        user = userService.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
