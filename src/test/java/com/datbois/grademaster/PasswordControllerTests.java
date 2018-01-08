package com.datbois.grademaster;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

public class PasswordControllerTests extends OAuthTests {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void userCanRequestRetardToken() {
        User user = userService.findByEmail("john.doe@student.stenden.com");

        Map<String, String> data = new HashMap<>();
        data.put("email", user.getEmail());

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/v1/auth/retard")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        User testUser = userService.findById(user.getId());

        assertThat("RetardToken is set", testUser.getRetardToken(), notNullValue());
    }

    @Test
    public void userCanChangePassword() {
        User user = userService.findByEmail("john.doe@student.stenden.com");

        user.setRetardToken(UUID.randomUUID().toString());
        userService.save(user);

        Map<String, String> data = new HashMap<>();
        data.put("token", user.getRetardToken());
        data.put("email", user.getEmail());
        data.put("password", "SomeNewPassword");

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .patch("/api/v1/auth/retard")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        User testUser = userService.findById(user.getId());

        assertThat("Password is correct", passwordEncoder.matches(data.get("password"), testUser.getPassword()), is(true));
        assertThat("RetardToken is null", testUser.getRetardToken(), is(nullValue()));
    }
}
