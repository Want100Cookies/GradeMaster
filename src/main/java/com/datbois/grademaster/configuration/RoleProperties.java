package com.datbois.grademaster.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "domain")
@Configuration
public class RoleProperties {

    private String student;
    private String teacher;

    public String getStudent() {
        return student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
