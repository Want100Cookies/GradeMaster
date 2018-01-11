package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity getGroups(Authentication authentication) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();
        Set<Role> roles = user.getRoles();
        if (roles.stream().filter(role -> role.getCode().equalsIgnoreCase("ADMIN_ROLE")).findFirst().orElse(null) != null) {
            return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>(user.getGroups(), HttpStatus.OK);
    }

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public ResponseEntity createGroup(@RequestBody Group group) {
        if (group == null || !group.isValid())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(groupService.save(group), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/groups/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public ResponseEntity changeGroup(@PathVariable Long id, @RequestBody Group group) throws Exception {
        Group existing = groupService.findById(id);
        if (existing == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        group.copyNonNullProperties(existing);
        return new ResponseEntity<>(groupService.save(existing), HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public ResponseEntity deleteGroup(@PathVariable Long id) {
        Group existing = groupService.findById(id);
        if (existing == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{id}/users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE') or isInGroup(#id)")
    public ResponseEntity getGroupUsers(@PathVariable Long id) {
        Group group = groupService.findById(id);
        if (group == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(group.getUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{id}/users", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    public ResponseEntity addGroupUsers(@PathVariable Long id, @RequestBody Set<User> users) {
        Group group = groupService.findById(id);
        if (group == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        group.setUsers(users);
        return new ResponseEntity<>(groupService.save(group), HttpStatus.OK);
    }

}