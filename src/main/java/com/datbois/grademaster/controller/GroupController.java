package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import com.datbois.grademaster.service.GroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all groups for user.",
            notes = "Only admin || user in own group."
    )
    public List<Group> getGroups(Authentication authentication) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();

        if (user.hasAnyRole("ADMIN_ROLE")) {
            return groupService.findAll();
        }

        return new ArrayList<>(user.getGroups());
    }

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Create a group",
            notes = "Only admin || teacher."
    )
    public Group createGroup(@RequestBody @NotNull Group group) {
        return groupService.save(group);
    }

    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or isInGroup(#groupId)")
    @ApiOperation(
            value = "Get a specific group.",
            notes = "Only admin || user in group."
    )
    public Group getGroup(@PathVariable Long groupId) {
        return groupService.findById(groupId);
    }

    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or (hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId))")
    @ApiOperation(
            value = "Update a group",
            notes = "Only admin || teacher in group."
    )
    public Group changeGroup(@PathVariable Long groupId, @RequestBody Group group) throws Exception {
        Group existing = groupService.findById(groupId);
        if (existing == null) throw new InvalidParameterException("Group id not found");
        group.copyNonNullProperties(existing);
        return groupService.save(existing);
    }

    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or (hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId))")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a group.",
            notes = "Only admin || teacher in group."
    )
    public void deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
    }

    @RequestMapping(value = "/groups/{groupId}/users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or isInGroup(#groupId)")
    @ApiOperation(
            value = "Get users of a group.",
            notes = "Only admin || user in group."
    )
    public Set<User> getGroupUsers(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);
        if (group == null) throw new InvalidParameterException("Group id not found");
        return group.getUsers();
    }

    @RequestMapping(value = "/groups/{groupId}/users", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or (hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId))")
    @ApiOperation(
            value = "Set users of a group.",
            notes = "Only admin || teacher in group."
    )
    public Group setGroupUsers(@PathVariable Long groupId, @RequestBody Set<User> users) {
        Group group = groupService.findById(groupId);
        if (group == null) throw new InvalidParameterException("Group id not found");
        group.setUsers(users);
        return groupService.save(group);
    }

}