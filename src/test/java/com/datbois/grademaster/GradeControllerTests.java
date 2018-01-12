package com.datbois.grademaster;

import com.datbois.grademaster.service.GradeService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GradeControllerTests extends OAuthTests {
    @Autowired
    GradeService gradeService;

    @Test
    public void TeacherCanInsertGroupGrade() {
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 9.5f);
        gradeData.put("comment", "Well Done!");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(gradeData)
                .when()
                .patch("/api/v1/grade/group/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("grade", equalTo(gradeData.get("grade")));
    }

//    @Test
//    public void adminCanViewGrade() {
//        String token = this.obtainAccessToken("admin@stenden.com", "password");
//
//        Grade grade = gradeService.findById(Long.parseLong("0"));
//
//        given()
//                .auth()
//                .oauth2(token)
//                .when()
//                .get("/api/v1/grade/" + grade.getId())
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("grade", is(grade.getGrade()));
//    }

}
