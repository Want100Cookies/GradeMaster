package com.datbois.grademaster.configuration;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private DomainProperties domainProperties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("grademaster-api")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build()
                .securitySchemes(Collections.singletonList(oauth()));

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Grade Master API")
                .description("This is the Grade Master API documentation")
                .build();
    }

    private List<GrantType> grantTypes() {
        return Collections.singletonList(new ResourceOwnerPasswordCredentialsGrant(domainProperties.getBase() + "/oauth/token"));
    }

    private List<AuthorizationScope> scopes() {
        List<AuthorizationScope> list = new ArrayList<>();
        list.add(new AuthorizationScope("read", "Grants read access"));
        list.add(new AuthorizationScope("write", "Grants write access"));
        return list;
    }

    private SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("OAuth2")
                .scopes(scopes())
                .grantTypes(grantTypes())
                .build();
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        String clientId = "grademaster-client";
        String clientSecret = "grademaster-secret";
        return new SecurityConfiguration(
                clientId,
                clientSecret,
                "realm",
                clientId,
                "apiKey",
                ApiKeyVehicle.HEADER,
                "api_key",
                " "
        );
    }
}