package com.datbois.grademaster;

import com.datbois.grademaster.model.Education;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.CourseService;
import com.datbois.grademaster.service.EducationService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EducationControllerTests extends OAuthTests {

    @Autowired
    private EducationService educationService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Test
    public void userCanViewAllEducations() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/educations")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void userCanViewSingleEducation() {
        String token = obtainAccessToken("admin@stenden.com", "password");
        Education education = educationService.findById(1L);

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/educations/{educationId}", education.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is(education.getName()));
    }

    @Test
    public void adminCanCreateEducation() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Map<String, String> data = new HashMap<>();
        data.put("name", "Hotel school");

        Integer id =
                given()
                        .auth()
                        .oauth2(token)
                        .contentType(ContentType.JSON)
                        .body(data)
                        .when()
                        .post("/api/v1/educations")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .contentType(ContentType.JSON)
                        .body("name", is(data.get("name")))
                        .extract()
                        .path("id");

        Education testEducation = educationService.findById(new Long(id));

        assertThat(testEducation.getName(), is(data.get("name")));
    }

    @Test
    public void adminCanUpdateEducation() {
        String token = obtainAccessToken("admin@stenden.com", "password");

        Education education = educationService.findById(1L);

        Map<String, String> data = new HashMap<>();
        data.put("name", "New Name");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .patch("/api/v1/educations/{educationId}", education.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        Education testEducation = educationService.findById(education.getId());

        assertThat(testEducation.getName(), is(data.get("name")));
    }

    @Test
    public void adminCanDeleteEducation() {
        User user = userService.findByEmail("admin@stenden.com");
        String token = obtainAccessToken(user.getEmail(), "password");

        Education education = educationService.findById(1L);
        long courseCount = courseService.count();
        long groupCount = groupService.count();

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/educations/{educationId}", education.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        assertThat(educationService.findById(education.getId()), is(nullValue()));
        assertThat("Education deletion results in course deletion", courseService.count(), lessThan(courseCount));
        assertThat("Course deletion results in group deletion", groupService.count(), lessThan(groupCount));
        assertThat("Course deletion results in no user deletion", userService.findById(user.getId()), notNullValue());
    }

    @Test
    public void userCanGetCoursesInEducation() {
        String token = obtainAccessToken("john.doe@student.stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/educations/{educationId}/courses", 1L)
                .then()
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }
}
