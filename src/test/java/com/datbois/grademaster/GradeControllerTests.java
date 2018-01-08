package com.datbois.grademaster;

import com.datbois.grademaster.service.GradeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

public class GradeControllerTests extends OAuthTests{
    @Autowired
    GradeService gradeService;

    @Test
    public void adminCanViewGrade() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/grade/1")
                .then()
                .body("size()", greaterThan(0));
    }

    }
