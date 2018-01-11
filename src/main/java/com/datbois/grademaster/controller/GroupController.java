package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity groups() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
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

}