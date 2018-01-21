package com.datbois.grademaster.controller;

import com.datbois.grademaster.configuration.DomainProperties;
import com.datbois.grademaster.exception.BadRequestException;
import com.datbois.grademaster.model.*;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.RoleService;
import com.datbois.grademaster.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    private DomainProperties domainProperties;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ApiOperation(
            value = "Get all users in the application including their groups.",
            notes = "Only works as admin or teacher"
    )
    public List<User> users(@RequestParam(required = false) String role) {
        if (role != null) {
            return userService.findByRole(roleService.findByName(role));
        }

        return userService.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isCurrentUser(#userId)")
    @ApiOperation(
            value = "Retrieve a single user and his group.",
            notes = "But only retrieve this user if it is the currently logged in user, or the logged in user is an teacher/admin."
    )
    public User user(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @RequestMapping(value = "/users/self", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieve the currently logged in user")
    public User currentUser(Authentication authentication) {
        return ((UserDetails) authentication.getPrincipal()).getUser();
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Create a new user using the RequestBody.",
            notes = "The first user that registers with the application becomes admin. Also the role is matched using the email address provided."
    )
    public User createUser(@RequestBody User user) {
        // Set email to unverified and generate random string for the verification email
        user.setVerified(false);
        user.setEmailVerifyToken(UUID.randomUUID().toString());
        user.setName(WordUtils.capitalize(user.getEmail().replace('.', ' ').split("@")[0]));

        // Determine what roles need to be assigned
        Set<Role> roles = new HashSet<>();

        if (userService.count() == 0) {
            roles.add(roleService.findByName("Admin"));
        }

        Pattern pattern = Pattern.compile(String.format(
                "([A-Z|a-z|0-9](\\.|_)?)+[A-Z|a-z|0-9]\\@(%s|%s)",
                domainProperties.getStudent().replace(".", "\\."),
                domainProperties.getTeacher().replace(".", "\\.")
        ));

        Matcher matcher = pattern.matcher(user.getEmail());

        if (matcher.find()) {
            if (matcher.group(3).equals(domainProperties.getStudent())) {
                roles.add(roleService.findByName("Student"));
            }

            if (matcher.group(3).equals(domainProperties.getTeacher())) {
                roles.add(roleService.findByName("Teacher"));
            }
        } else {
            throw new BadRequestException("Email address not accepted");
        }

        user.setRoles(roles);

        user = userService.save(user);

        Email verificationMail = new Email(
                user.getEmail(),
                "Verify your email address",
                "Click the button below to verify your email address.",
                domainProperties.getBase() + String.format("/#!/verify?email=%s&token=%s", user.getEmail(), user.getEmailVerifyToken()),
                "Verify"
        );

        emailService.sendToEmailQueue(verificationMail);

        return user;
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isCurrentUser(#userId)")
    @ApiOperation(
            value = "Update a user with only the given fields.",
            notes = "An student can only update himself and an teacher/admin can update any user."
    )
    public User updateSingleUser(@PathVariable Long userId, @RequestBody User user) throws Exception {
        User existing = userService.findById(userId);
        boolean verified = existing.isVerified();

        user.copyNonNullProperties(existing);

        // Unfortunately because verified is a boolean, the requestbody set's it to false and not null and therefore is copied to the existing model
        existing.setVerified(verified);

        return userService.save(existing);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Remove a user from the database.",
            notes = "Only admins can delete users."
    )
    public void removeUser(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @RequestMapping(value = "/users/{userId}/groups", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isCurrentUser(#userId)")
    @ApiOperation(value = "Get all groups assigned to a user")
    public Set<Group> getGroupsForUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return user.getGroups();
    }
}
