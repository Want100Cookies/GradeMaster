package com.datbois.grademaster;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.Period;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.CourseService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GroupControllerTests extends OAuthTests {

    @Autowired
    private GroupService groupService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    private int userCount;

    @Before
    public void getUserCount() {
        userCount = (int) userService.count();
    }

    @After
    public void checkUserCount() {
        int newUserCount = (int) userService.count();
        assertThat(userCount, equalTo(newUserCount));
    }

    @Test
    public void studentCanOnlyGetOwnGroups() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        int count = (int) groupService.count();
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

        int count = (int) groupService.count();
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

        int count = (int) groupService.count();
        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", equalTo(count));
    }

    @Test
    public void studentCantCreateGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        long count = (int) groupService.count();

        Course course = courseService.findById(1L);

        Group g = new Group(2017, 2018, new HashSet<>(Arrays.asList(Period.Q1, Period.Q2)), course, "Dew u no dewey", null);

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .post("/api/v1/groups")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        assertThat(count, is(groupService.count()));
    }

    @Test
    public void teacherCanCreateGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        long count = (int) groupService.count();

        Course course = courseService.findById(1L);

        Group g = new Group(2017, 2018, new HashSet<>(Arrays.asList(Period.Q1, Period.Q2)), course, "Dew u no dewey", null);

        Integer id = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .post("/api/v1/groups")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .extract()
                .path("id");

        Group group = groupService.findById(new Long(id));

        assertThat(group.getGroupName(), is(g.getGroupName()));
        assertThat(group.getCourse(), notNullValue());
        assertThat(count, lessThan(groupService.count()));
    }

    @Test
    public void adminCanCreateGroup() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        long count = (int) groupService.count();

        Course course = courseService.findById(1L);

        Group g = new Group(2017, 2018, new HashSet<>(Arrays.asList(Period.Q1, Period.Q2)), course, "Dew u no dewey", null);

        Integer id = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .post("/api/v1/groups")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .extract()
                .path("id");

        Group group = groupService.findById(new Long(id));

        assertThat(group.getGroupName(), is(g.getGroupName()));
        assertThat(group.getCourse(), notNullValue());
        assertThat(count, lessThan(groupService.count()));
    }

    @Test
    public void studentCanGetOwnSpecificGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void studentCantGetOtherSpecificGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void teacherCanGetOwnSpecificGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void teacherCantGetOtherSpecificGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void adminCanGetAllSpecificGroups() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());

        g = groupService.findById(2L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());

        g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}", g.getId())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void studentCantEditGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(1L);

        g.setGroupName("But can you do this");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .patch("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .contentType(ContentType.JSON);

        Group group = groupService.findById(1L);

        assertThat(group.getGroupName(), not(g.getGroupName()));
    }

    @Test
    public void teacherCanEditOwnGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(3L);

        g.setGroupName("But can you do this");

        Integer id = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .patch("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .path("id");

        Group group = groupService.findById(new Long(id));

        assertThat(group.getGroupName(), is(g.getGroupName()));
    }

    @Test
    public void teacherCantEditOtherGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(1L);

        g.setGroupName("But can you do this");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .patch("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void adminCanEditGroup() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Group g = groupService.findById(1L);

        g.setGroupName("But can you do this");

        Integer id = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(g)
                .when()
                .patch("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .path("id");

        Group group = groupService.findById(new Long(id));

        assertThat(group.getGroupName(), is(g.getGroupName()));
    }

    @Test
    public void studentCantDeleteGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .contentType(ContentType.JSON);

        Group group = groupService.findById(1L);

        assertThat(group, notNullValue());
    }

    @Test
    public void teacherCanDeleteOwnGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        Group group = groupService.findById(g.getId());

        assertThat(group, is(nullValue()));
    }

    @Test
    public void teacherCantDeleteOtherGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void adminCanDeleteGroup() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/groups/{groupId}", g.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        Group group = groupService.findById(g.getId());

        assertThat(group, is(nullValue()));
    }

    @Test
    public void studentCanGetUsersInOwnGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void studentCantGetUsersInOtherGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void teacherCanGetUsersInOwnGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void teacherCantGetUsersInOtherGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void adminCanGetUsersInOwnGroup() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Group g = groupService.findById(3L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void adminCanGetUsersInOtherGroup() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Group g = groupService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void studentCantChangeUsersInGroup() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        Group g = groupService.findById(1L);

        Set<User> users = new HashSet<>(Arrays.asList(userService.findById(1L), userService.findById(2L), userService.findById(3L)));

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(users)
                .when()
                .patch("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void teacherCanChangeUsersInOwnGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(3L);

        Set<User> users = new HashSet<>(Arrays.asList(userService.findById(1L), userService.findById(2L), userService.findById(3L)));

        Integer id = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(users)
                .when()
                .patch("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .path("id");

        Group group = groupService.findById(new Long(id));

        assertThat(group.getUsers().size(), equalTo(users.size()));
        assertThat(g.getUsers().size(), lessThanOrEqualTo(group.getUsers().size()));
    }

    @Test
    public void teacherCantChangeUsersInOtherGroup() {
        String token = obtainAccessToken("jane.doe@stenden.com", "password");

        Group g = groupService.findById(1L);

        Set<User> users = new HashSet<>(Arrays.asList(userService.findById(1L), userService.findById(2L), userService.findById(3L)));

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(users)
                .when()
                .patch("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void adminCanChangeUsersInGroup() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Group g = groupService.findById(2L);

        Set<User> users = new HashSet<>(Arrays.asList(userService.findById(1L), userService.findById(2L), userService.findById(3L)));

        Integer id = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(users)
                .when()
                .patch("/api/v1/groups/{groupId}/users", g.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .path("id");

        Group group = groupService.findById(new Long(id));

        assertThat(group.getUsers().size(), equalTo(users.size()));
        assertThat(g.getUsers().size(), lessThanOrEqualTo(group.getUsers().size()));
    }
}
