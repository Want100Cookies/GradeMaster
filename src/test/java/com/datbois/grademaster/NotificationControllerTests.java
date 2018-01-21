package com.datbois.grademaster;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.model.User;
import com.datbois.grademaster.service.NotificationService;
import com.datbois.grademaster.service.UserService;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import javax.naming.AuthenticationNotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class NotificationControllerTests extends OAuthTests {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Test
    public void retrieveAllNotifications(){
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/notifications")
                .then()
                .body("size()", greaterThan(0));
    }

    @Test
    public void updateAllNotifications(){
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        User user = userService.findByEmail("john.doe@student.stenden.com");

        List<Notification> notifications = user.getNotificationList();

        given()
                .auth()
                .oauth2(token)
                .when()
                .patch("/api/v1/notifications")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        for(Notification n : notifications){
            assertThat(n.isSeen(), equalTo(true));
        }
    }

    @Test
    public void updateNotification(){
        String token = this.obtainAccessToken("john.doe@student.stenden.com", "password");

        Notification notification = notificationService.findById(1L);

        Map<String, Boolean> data = new HashMap<>();
        data.put("seen", true);

        HashMap<String, Boolean> result = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .patch("/api/v1/notifications/{notificationId}", notification.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .extract()
                .path("$");

        assertThat("Notification - Marked as seen", result.get("seen"), Matchers.is(data.get("seen")));
    }

}
