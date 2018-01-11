package com.datbois.grademaster;

import com.datbois.grademaster.service.GroupService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class GroupControllerTests extends OAuthTests {

    @Autowired
    private GroupService groupService;

    @Test
    public void studentCanOnlyGetOwnGroups() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        long count = groupService.count();
        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", lessThan(count));
    }

    @Test
    public void teacherCanOnlyGetOwnGroups() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        long count = groupService.count();
        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", lessThan(count));
    }

    @Test
    public void adminCanGetAllGroups() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        long count = groupService.count();
        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", equalTo(count));
    }
}
