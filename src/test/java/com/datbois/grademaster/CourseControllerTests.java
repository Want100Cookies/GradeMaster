package com.datbois.grademaster;

import com.datbois.grademaster.model.Course;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.CourseService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourseControllerTests extends OAuthTests {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Test
    public void userCanViewAllGroups() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/courses")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void userCanViewSingleCourse() {
        String token = obtainAccessToken("admin@stenden.com", "password");
        Course course = courseService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/courses/{courseId}", course.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is(course.getName()));
    }

    @Test
    public void adminCanCreateCourse() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Map<String, String> data = new HashMap<>();
        data.put("name", "Course Name");

        Integer id =
                given()
                        .auth()
                        .oauth2(token)
                        .contentType(ContentType.JSON)
                        .body(data)
                        .when()
                        .post("/api/v1/courses")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .contentType(ContentType.JSON)
                        .body("name", is(data.get("name")))
                        .extract()
                        .path("id");

        Course testCourse = courseService.findById(new Long(id));

        assertThat(testCourse.getName(), is(data.get("name")));
    }

    @Test
    public void adminCanUpdateCourse() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Course course = courseService.findById(1L);

        Map<String, String> data = new HashMap<>();
        data.put("name", "New Name");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .patch("/api/v1/courses/{courseId}", course.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        Course testCourse = courseService.findById(course.getId());

        assertThat(testCourse.getName(), is(data.get("name")));
    }

    @Test
    public void adminCanDeleteCourse() {
        User user = userService.findByEmail("admin@stenden.com");
        String token = obtainAccessToken(user.getEmail(), "password");

        Course course = courseService.findById(1L);
        long groupCount = groupService.count();

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/courses/{courseId}", course.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        assertThat(courseService.findById(course.getId()), is(nullValue()));
        assertThat("Course deletion results in group deletion", groupService.count(), lessThan(groupCount));
        assertThat("Course deletion results in no user deletion", userService.findById(user.getId()), notNullValue());
    }
}
