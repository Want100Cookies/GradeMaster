package com.datbois.grademaster;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.response.GradeResponse;
import com.datbois.grademaster.service.*;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


public class GradeControllerTests extends OAuthTests {
    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupGradeService groupGradeService;

    @MockBean
    private EmailService emailService;

    private Group createFreshGroup(User teacher, User student1, User student2) {
        return groupService.save(new Group(
                2017, 2018,
                new HashSet<>(Collections.singleton(Period.Q1)),
                courseService.findById(1L),
                "Foo", new HashSet<>(Arrays.asList(teacher, student1, student2))
        ));
    }

    private User createStudent(String name) {
        return userService.save(new User(
                name, name.replace(' ', '.') + "@student.stenden.com",
                null, "password", true, null,
                new HashSet<>(Collections.singletonList(roleService.findByName("Student"))),
                new HashSet<>())
        );
    }

    private GroupGrade createGroupGrade(Group group, User teacher) {
        GroupGrade groupGrade = groupGradeService.save(new GroupGrade(8.2, "Very welll", group, teacher, new DateTime().plusMonths(1).toDateTime()));
        group.setGroupGrade(groupGrade);
        groupService.save(group);

        return groupGrade;
    }

    @Test
    public void StudentCanGetGradeStatusInactive() {
        User jane = userService.findByEmail("jane.doe@stenden.com");
        User john = userService.findByEmail("john.doe@student.stenden.com");
        User foo = createStudent("Foo User");
        Group group = createFreshGroup(jane, john, foo);

        String token = this.obtainAccessToken(john.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.INACTIVE.name()));
    }

    @Test
    public void StudentCanGetGradeStatusOpen() {
        User jane = userService.findByEmail("jane.doe@stenden.com");
        User john = userService.findByEmail("john.doe@student.stenden.com");
        User foo = createStudent("Foo User");
        Group group = createFreshGroup(jane, john, foo);
        createGroupGrade(group, jane);

        String token = this.obtainAccessToken(john.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.OPEN.name()));

        verify(emailService, times(2)).sendToEmailQueue(ArgumentMatchers.any(Email.class));
    }

    @Test
    public void StudentCanGetGradeStatusPending() {
        User jane = userService.findByEmail("jane.doe@stenden.com");
        User john = userService.findByEmail("john.doe@student.stenden.com");
        User foo = createStudent("Foo User");
        Group group = createFreshGroup(jane, john, foo);
        createGroupGrade(group, jane);

        gradeService.save(new Grade(8.2, "foo", john, foo, group));
        gradeService.save(new Grade(8.2, "foo", john, john, group));

        gradeService.save(new Grade(8.2, "bar", foo, foo, group));
        gradeService.save(new Grade(8.2, "bar", foo, john, group));

        String token = this.obtainAccessToken(john.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.PENDING.name()));

        verify(emailService, times(3)).sendToEmailQueue(ArgumentMatchers.any(Email.class));
    }

    @Test
    public void UserCanGetGradeStatusClosed() {
        User jane = userService.findByEmail("jane.doe@stenden.com");
        User john = userService.findByEmail("john.doe@student.stenden.com");
        User foo = createStudent("Foo User");
        Group group = createFreshGroup(jane, john, foo);
        createGroupGrade(group, jane);

        gradeService.save(new Grade(8.2, "", jane, foo, group));
        gradeService.save(new Grade(8.2, "", jane, john, group));

        String token = this.obtainAccessToken(john.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.CLOSED.name()));

        token = this.obtainAccessToken(jane.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.CLOSED.name()));
    }

    @Test
    public void TeacherCanGetGradeStatusOpen() {
        User jane = userService.findByEmail("jane.doe@stenden.com");
        User john = userService.findByEmail("john.doe@student.stenden.com");
        User foo = createStudent("Foo User");
        Group group = createFreshGroup(jane, john, foo);

        String token = this.obtainAccessToken(jane.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.OPEN.name()));
    }

    @Test
    public void TeacherCanGetGradeStatusPending() {
        User jane = userService.findByEmail("jane.doe@stenden.com");
        User john = userService.findByEmail("john.doe@student.stenden.com");
        User foo = createStudent("Foo User");
        Group group = createFreshGroup(jane, john, foo);
        createGroupGrade(group, jane);

        String token = this.obtainAccessToken(jane.getEmail(), "password");

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/v1/grades/status/groups/{groupId}", group.getId())
                .then()
                .body("status", is(Status.PENDING.name()));
    }

    @Test
    public void StudentInsertGradeWithoutMotivationDoesNotCount() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

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
                .post("/api/v1/grades/groups/" + group.getId())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("$");

        assertThat(result.get(0).get("valid"), Matchers.is(false));
    }

    @Test
    public void TeacherInsertGradeWithoutMotivation() {
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
                .post("/api/v1/grades/groups/" + group.getId())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("$");

        assertThat(result.get(0).get("valid"), Matchers.is(true));
    }

    @Test
    public void StudentCanGetFinalGradeForGroup() {
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        Long uId = 1L;
        Long gId = 2L;

        Grade finalGrade = null;

        Group group = groupService.findById(gId);
        User user = userService.findById(uId);

        for (User u : group.getUsers()) {
            if (u.getId() == user.getId()) {
                for (Grade grade : group.getGrades()) {
                    for (Role role : grade.getFromUser().getRoles()) {
                        if (role.getCode().contains("TEACHER_ROLE")) {
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
                .get("/api/v1/grades/groups/" + gId + "/users/" + uId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("$");


        System.out.println(result);
        assertThat(result.get("grade"), Matchers.is(Float.parseFloat(finalGrade.getGrade().toString())));
    }

    @Test
    public void TeacherCanGetAllGradesFromAGroup() {
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
                .statusCode(HttpStatus.CREATED.value())
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
        gradeData.put("grade", 3.0);
        gradeData.put("motivation", "pleb");
        gradeData.put("fromUser", fromUser);
        gradeData.put("toUser", toUser);

        List<Map<String, Object>> grades = new ArrayList<>();
        grades.add(gradeData);

        GradeResponse[] result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(grades)
                .post("/api/v1/grades/groups/" + group.getId())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(GradeResponse[].class);

        GradeResponse gradeResponse = result[0];

        assertThat(gradeResponse.getGrade(), Matchers.is(gradeData.get("grade")));
        assertThat(gradeResponse.getMotivation(), Matchers.is(gradeData.get("motivation")));
        assertThat(gradeResponse.getFromUser().getId(), Matchers.is(fromUser.getId()));
        assertThat(gradeResponse.getToUser().getId(), Matchers.is(toUser.getId()));
        assertThat(gradeResponse.getGroup().getId(), Matchers.is(group.getId()));
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
