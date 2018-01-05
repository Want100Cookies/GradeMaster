package com.datbois.grademaster.configuration;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.model.UserDetails;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isCurrentUser(Long userId) {
        User user = ((UserDetails) getPrincipal()).getUser();

        return user.getId().equals(userId);
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
