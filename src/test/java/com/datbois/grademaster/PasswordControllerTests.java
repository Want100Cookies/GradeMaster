package com.datbois.grademaster;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class PasswordControllerTests extends OAuthTests {

    @Autowired
    private UserService userService;

    @Test
    public void userCanRequestRetardToken() {
        User user = userService.findByEmail("john.doe@student.stenden.com");

        Map<String, String> data = new HashMap<>();
        data.put("email", user.getEmail());

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .post("/api/v1/auth/retard")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        User testUser = userService.findById(user.getId());

        assertThat("RetartToken is set", testUser.getRetardToken(), notNullValue());
    }
}
