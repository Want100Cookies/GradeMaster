package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity<?> groups() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody Group group) {
        if (group == null || !group.isValid())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        groupService.save(group);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}