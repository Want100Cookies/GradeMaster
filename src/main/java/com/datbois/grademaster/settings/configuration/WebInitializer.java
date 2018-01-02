package com.datbois.grademaster.settings.configuration;

import com.datbois.grademaster.GradeMasterApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class WebInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GradeMasterApplication.class);
    }
}
