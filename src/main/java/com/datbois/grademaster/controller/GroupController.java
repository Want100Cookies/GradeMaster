package com.datbois.grademaster.controller;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
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

    @Autowired
    private UserService userService;

    /**
     * Get all groups for user.
     * Only admin || user in own group.
     *
     * @return All groups for user
     * @endpoint (GET) /api/v1/groups
     * @responseStatus OK
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public List<Group> getGroups(Authentication authentication) {
        User user = ((UserDetails) authentication.getPrincipal()).getUser();
        Set<Role> roles = user.getRoles();
        if (roles.stream().filter(role -> role.getCode().equalsIgnoreCase("ADMIN_ROLE")).findFirst().orElse(null) != null) {
            return groupService.findAll();
        }
        return new ArrayList<>(user.getGroups());
    }

    /**
     * Create a group
     * Only admin || teacher.
     *
     * @param group group object to be created
     * @return The created group
     * @endpoint (POST) /api/v1/groups
     * @responseStatus CREATED
     */
    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('TEACHER_ROLE', 'ADMIN_ROLE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody @NotNull Group group) {
        return groupService.save(group);
    }

    /**
     * Get a specific group.
     * Only admin || user in group.
     *
     * @param groupId id of the group
     * @return group
     * @endpoint (GET) /api/v1/groups/{groupId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or isInGroup(#groupId)")
    public Group changeGroup(@PathVariable Long groupId) {
        return groupService.findById(groupId);
    }

    /**
     * Update a group.
     * Only admin || teacher in group.
     *
     * @param groupId id of the group
     * @param group group object to be updated to
     * @return group
     * @throws Exception
     * @endpoint (PATCH) /api/v1/groups/{groupId}
     * @responseStatus OK
     */
    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or (hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId))")
    public Group changeGroup(@PathVariable Long groupId, @RequestBody Group group) throws Exception {
        Group existing = groupService.findById(groupId);
        if (existing == null) throw new InvalidParameterException("Group id not found");
        group.copyNonNullProperties(existing);
        return groupService.save(existing);
    }

    /**
     * Delete a group.
     * Only admin || teacher in group.
     *
     * @param groupId id of the group
     * @endpoint (DELETE) /api/v1/groups/{groupId}
     * @responseStatus ACCEPTED
     */
    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or (hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId))")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteGroup(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);
        for (User u : group.getUsers()) { // Delete group from the users
            User user = userService.findById(u.getId());
            Set<Group> groups = user.getGroups();
            groups.remove(group);
            user.setGroups(groups);
            userService.save(user);
        }
        groupService.delete(groupId);
    }


    /**
     * Get users of a group.
     * Only admin || user in group.
     *
     * @param groupId id of the group
     * @return Set of the users
     * @endpoint (GET) /api/v1/groups/{groupId}/users
     * @responseStatus OK
     */
    @RequestMapping(value = "/groups/{groupId}/users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or isInGroup(#groupId)")
    public Set<User> getGroupUsers(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);
        if (group == null) throw new InvalidParameterException("Group id not found");
        return group.getUsers();
    }

    /**
     * Set users of a group.
     * Only admin || teacher in group.
     *
     * @param groupId id of the group
     * @return group
     * @endpoint (PATCH) /api/v1/groups/{groupId}/users
     * @responseStatus OK
     */
    @RequestMapping(value = "/groups/{groupId}/users", method = RequestMethod.PATCH)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE') or (hasAnyAuthority('TEACHER_ROLE') and isInGroup(#groupId))")
    public Group setGroupUsers(@PathVariable Long groupId, @RequestBody Set<User> users) {
        Group group = groupService.findById(groupId);
        if (group == null) throw new InvalidParameterException("Group id not found");
        group.setUsers(users);
        for (User u : users) {
            User user = userService.findById(u.getId());
            Set<Group> groups = user.getGroups();
            groups.add(group);
            user.setGroups(groups);
            userService.save(user);
        }
        return groupService.save(group);
    }

}