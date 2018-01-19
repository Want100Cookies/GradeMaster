package com.datbois.grademaster;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.model.Email;
import com.datbois.grademaster.model.Group;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.EmailService;
import com.datbois.grademaster.service.GradeService;
import com.datbois.grademaster.service.GroupService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;


public class GradeControllerTests extends OAuthTests {
    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @MockBean
    private EmailService emailService;

    @Test
    public void TeacherCanGetGradeStatus(){
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/3")
                .then()
                .body("status", is(Status.Pending.name()));
    }

    @Test
    public void StudentInsertGradeWithoutMotivationDoesNotCount(){
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

//        gradeService.delete(5L);

        User fromUser = userService.findById(1L);
        User toUser = userService.findById(1L);
        Group group = groupService.findById(2L);

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 3.0f);
        gradeData.put("motivation", "");
        gradeData.put("fromUser", fromUser);
        gradeData.put("toUser", toUser);

        List<Map<String, Object>> grades = new ArrayList<>();
        grades.add(gradeData);

        ArrayList<Map<String, Object>> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(grades)
                .post("/api/v1/grades/groups/"+group.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get(0).get("valid"), Matchers.is(false));
    }

    @Test
    public void TeacherInsertGradeWithoutMotivation(){
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        gradeService.delete(3L);

        User fromUser = userService.findById(2L);
        User toUser = userService.findById(1L);
        Group group = groupService.findById(2L);

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 3.0f);
        gradeData.put("motivation", "");
        gradeData.put("fromUser", fromUser);
        gradeData.put("toUser", toUser);

        List<Map<String, Object>> grades = new ArrayList<>();
        grades.add(gradeData);

        ArrayList<Map<String, Object>> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(grades)
                .post("/api/v1/grades/groups/"+group.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get(0).get("valid"), Matchers.is(true));
    }

    @Test
    public void StudentCanGetFinalGradeForGroup(){
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        Long uId = 1L;
        Long gId = 2L;

        Grade finalGrade = null;

        Group group = groupService.findById(gId);
        User user = userService.findById(uId);

        for(User u : group.getUsers()){
            if (u.getId() == user.getId()){
                for(Grade grade : group.getGrades()){
                    for(Role role : grade.getFromUser().getRoles()){
                        if(role.getCode().contains("TEACHER_ROLE")){
                            finalGrade = grade;
                        }
                    }
                }
            }
        }

        HashMap<String, Object> result = given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/grades/groups/"+gId+"/users/"+uId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");


        System.out.println(result);
        assertThat(result.get("grade"), Matchers.is(Float.parseFloat(finalGrade.getGrade().toString())));
    }

    @Test
    public void TeacherCanGetAllGradesFromAGroup(){
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/grades/groups/4")
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
                .patch("/api/v1/grades/groups/3")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get("grade"), Matchers.is(gradeData.get("grade")));
        assertThat(result.get("comment"), Matchers.is(gradeData.get("comment")));
    }

    @Test
    public void StudentCanInsertGrade() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        gradeService.delete(3L);

        User fromUser = userService.findById(1L);
        User toUser = userService.findById(1L);
        Group group = groupService.findById(2L);

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 3.0f);
        gradeData.put("motivation", "pleb");
        gradeData.put("fromUser", fromUser);
        gradeData.put("toUser", toUser);

        List<Map<String, Object>> grades = new ArrayList<>();
        grades.add(gradeData);

        ArrayList<Map<String, Object>> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(grades)
                .post("/api/v1/grades/groups/"+group.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");

        assertThat(result.get(0).get("grade"), Matchers.is(gradeData.get("grade")));
        assertThat(result.get(0).get("motivation"), Matchers.is(gradeData.get("motivation")));
        assertThat(Long.parseLong(result.get(0).get("fromUser").toString()), Matchers.is(fromUser.getId()));
        assertThat(Long.parseLong(result.get(0).get("toUser").toString()), Matchers.is(toUser.getId()));
        assertThat(Long.parseLong(result.get(0).get("group").toString()), Matchers.is(group.getId()));
        verify(emailService).sendToEmailQueue(ArgumentMatchers.any(Email.class));
    }

    @Test
    public void DeleteGrades() {
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/grades/groups/3")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        assertThat(gradeService.findById(5L), Matchers.is(nullValue()));
    }
}
