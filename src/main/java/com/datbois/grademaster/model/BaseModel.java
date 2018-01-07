package com.datbois.grademaster.model;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

abstract public class BaseModel {

    public void copyNonNullProperties(BaseModel target) throws Exception {
        BeanUtils.copyProperties(this, target, getNullProperties());
    }

    private String[] getNullProperties() throws Exception {
        PropertyDescriptor[] propDescArr = Introspector.getBeanInfo(this.getClass(), Object.class).getPropertyDescriptors();

        return Arrays.stream(propDescArr)
                .filter(getNullPredicate())
                .map(PropertyDescriptor::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    private Predicate<PropertyDescriptor> getNullPredicate() {
        return pd -> {
            boolean result = false;
            try {
                Method getterMethod = pd.getReadMethod();
                result = (getterMethod != null && getterMethod.invoke(this) == null);
            } catch (Exception e) {
                LoggerFactory.getLogger(this.getClass()).error("error invoking getter method");
            }
            return result;
        };
    }
}