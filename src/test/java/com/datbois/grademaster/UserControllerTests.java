package com.datbois.grademaster;

import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.model.Role;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.RoleService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;

public class UserControllerTests extends OAuthTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @Test
    public void adminCanViewAllUsers() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");
        long count = userService.count();

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", is((int) count));
    }

    @Test
    public void adminCanViewAllStudents() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users?role=student")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", is(1));
    }

    @Test
    public void teacherCanViewAllUsers() {
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");
        long count = userService.count();

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", is((int) count));
    }

    @Test
    public void studentCantViewAllUsers() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void studentCanGetSelfViaId() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        User user = userService.findByEmail("john.doe@student.stenden.com");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users/" + user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is(user.getName()));
    }

    @Test
    public void userCanGetSelf() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        User user = userService.findByEmail("john.doe@student.stenden.com");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users/self")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is(user.getName()));
    }

    @Test
    public void studentCantGetOtherUser() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        User user = userService.findByEmail("jane.doe@stenden.com");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users/" + user.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void adminCanGetOtherUser() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        User user = userService.findByEmail("jane.doe@stenden.com");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users/{userId}", user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is(user.getName()));
    }

    @Test
    public void studentCanCreateUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "Test Student");
        userData.put("email", "test.student@student.stenden.com");
        userData.put("referenceId", "123456789");
        userData.put("password", "password");

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body("name", is(userData.get("name")));

        User testUser = userService.findByEmail(userData.get("email"));
        Role role = roleService.findByName("Student");

        assertThat("Email", userData.get("email"), equalTo(testUser.getEmail()));
        assertThat("Role", testUser.getRoles().iterator().next().getCode(), equalTo(role.getCode()));
        assertThat("Password", userData.get("password"), not(testUser.getPassword()));
        verify(emailService).sendToEmailQueue(ArgumentMatchers.any(Email.class));
    }

    @Test
    public void teacherCanCreateUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "Test Teacher");
        userData.put("email", "test.teacher@stenden.com");
        userData.put("referenceId", "987654321");
        userData.put("password", "password");

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body("name", is(userData.get("name")));

        User testUser = userService.findByEmail(userData.get("email"));
        Role role = roleService.findByName("Teacher");

        assertThat("Role", testUser.getRoles().iterator().next().getCode(), equalTo(role.getCode()));
        verify(emailService).sendToEmailQueue(ArgumentMatchers.any(Email.class));
    }

    @Test
    public void userCanUpdateSingleField() {
        User testUser = userService.findByEmail("john.doe@student.stenden.com");
        String token = this.obtainAccessToken(testUser.getEmail(), "password");

        Map<String, String> userData = new HashMap<>();
        userData.put("name", "Test Student");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .patch("/api/v1/users/{userId}", testUser.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is(userData.get("name")));

        User updatedUser = userService.findById(testUser.getId());

        assertThat("Email stays the same", testUser.getEmail(), equalTo(updatedUser.getEmail()));
        assertThat("Verified stays the same", testUser.isVerified(), equalTo(updatedUser.isVerified()));
        assertThat("Password stays the same", testUser.getPassword(), equalTo(updatedUser.getPassword()));
        assertThat("Name is changed", updatedUser.getName(), equalTo(userData.get("name")));
    }

    @Test
    public void userCanUpdatePassword() {
        User testUser = userService.findByEmail("john.doe@student.stenden.com");
        String token = this.obtainAccessToken(testUser.getEmail(), "password");

        Map<String, String> userData = new HashMap<>();
        userData.put("password", "newPassword");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .patch("/api/v1/users/{userId}", testUser.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        User updatedUser = userService.findById(testUser.getId());

        assertThat("Password is changed", testUser.getPassword(), not(equalTo(updatedUser.getPassword())));
        assertThat("Password is correct", passwordEncoder.matches(userData.get("password"), updatedUser.getPassword()), is(true));
    }

    @Test
    public void adminCanDeleteUser() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        User testUser = userService.findByEmail("john.doe@student.stenden.com");

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/users/{userId}", testUser.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        assertThat("User", userService.findByEmail("john.doe@student.stenden.com"), is(nullValue()));
    }

    @Test
    public void userCanGetAssignedGroups() {
        User user = userService.findByEmail("john.doe@student.stenden.com");
        String token = this.obtainAccessToken(user.getEmail(), "password");

        int size = user.getGroups().size();

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/users/{userId}/groups", user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(size));
    }
}
