package com.datbois.grademaster.configuration;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Set;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isCurrentUser(Long userId) {
        return getUser().getId().equals(userId);
    }

    public boolean isInGroup(Long groupId) {
        User user = getUser();
        Set<Group> groups = user.getGroups();
        return groups.stream()
                .filter(group -> group.getId().equals(groupId))
                .findFirst().orElse(null) != null;
    }

    private User getUser() {
        return ((UserDetails) getPrincipal()).getUser();
    }

    @Override
    public void setFilterObject(Object o) {
        filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object o) {
        returnObject = o;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
