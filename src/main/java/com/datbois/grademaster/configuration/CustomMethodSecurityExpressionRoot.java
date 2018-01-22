package com.datbois.grademaster.configuration;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import com.datbois.grademaster.service.UserService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserService userService;

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isCurrentUser(Long userId) {
        return getUser().getId().equals(userId);
    }

    public boolean isInGroup(Long groupId) {
        return getUser().isInGroup(groupId);
    }

    private User getUser() {
        return userService
                .findById(
                        ((UserDetails) getPrincipal())
                                .getUser()
                                .getId()
                );
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
