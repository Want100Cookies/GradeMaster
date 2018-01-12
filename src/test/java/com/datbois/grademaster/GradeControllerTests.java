package com.datbois.grademaster;

import com.datbois.grademaster.model.Grade;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.GradeService;
import com.fasterxml.jackson.core.JsonParser;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class GradeControllerTests extends OAuthTests{
    @Autowired
    GradeService gradeService;

    Matcher<Double> isDouble(double value) {
        return is(Double.valueOf(value));
    }

    @Test
    public void TeacherCanInsertGroupGrade(){
        String token = this.obtainAccessToken("jane.doe@stenden.com", "password");

        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("grade", 9.0D);
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
                .body("grade", isDouble((double)gradeData.get("grade")));
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
