package com.datbois.grademaster;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.GroupGrade;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.junit.Test;
import org.mockito.internal.matchers.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.jws.soap.SOAPBinding;
import javax.transaction.Transactional;
import java.util.*;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;

public class GradeControllerTests extends OAuthTests {
    @Autowired
    GradeService gradeService;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Test
    public void TeacherCanGetAllGradesFromAGroup(){
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/grade/group/3")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

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

        List<Map<String, Object>> grades = new ArrayList<>();
        grades.add(gradeData);

        ArrayList<Map<String, Object>> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(grades)
                .post("/api/v1/grade/users/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get(0).get("grade"), Matchers.is(gradeData.get("grade")));
        assertThat(result.get(0).get("motivation"), Matchers.is(gradeData.get("motivation")));
        assertThat(Long.parseLong(result.get(0).get("fromUser").toString()), Matchers.is(fromUser.getId()));
        assertThat(Long.parseLong(result.get(0).get("toUser").toString()), Matchers.is(toUser.getId()));
        assertThat(Long.parseLong(result.get(0).get("group").toString()), Matchers.is(group.getId()));
    }

    @Test
    public void DeleteGrades() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/grade/group/1")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        assertThat(gradeService.findById(1L), Matchers.is(nullValue()));
    }
}
