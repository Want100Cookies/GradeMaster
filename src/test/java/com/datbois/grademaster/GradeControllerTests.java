package com.datbois.grademaster;

import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class GradeControllerTests extends OAuthTests {
    @Autowired
    GradeService gradeService;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Test
    public void TeacherCanInsertGroupGrade() {
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 9.5f);
        gradeData.put("comment", "Well Done!");

        HashMap<String, Object> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(gradeData)
                .when()
                .patch("/api/v1/grade/group/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get("grade"), Matchers.is(gradeData.get("grade")));
        assertThat(result.get("comment"), Matchers.is(gradeData.get("comment")));
    }

    @Test
    public void StudentCanInsertGrade(){
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        User fromUser = userService.findById(1L);
        User toUser = userService.findById(1L);
        Group group = groupService.findById(1L);

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 3.0f);
        gradeData.put("motivation", "pleb");
        gradeData.put("fromUser", fromUser);
        gradeData.put("toUser", toUser);
        gradeData.put("group", group);

        HashMap<String, Object> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(gradeData)
                .post("/api/v1/grade")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get("grade"), Matchers.is(gradeData.get("grade")));
        assertThat(result.get("motivation"), Matchers.is(gradeData.get("motivation")));
        assertThat(Long.parseLong(result.get("fromUser").toString()), Matchers.is(fromUser.getId()));
        assertThat(Long.parseLong(result.get("toUser").toString()), Matchers.is(toUser.getId()));
        assertThat(Long.parseLong(result.get("group").toString()), Matchers.is(group.getId()));
    }
}
