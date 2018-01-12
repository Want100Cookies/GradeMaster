package com.datbois.grademaster;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class NotificationControllerTests extends OAuthTests {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void retrieveAllNotifications(){
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/notifications")
                .then()
                .body("size()", greaterThan(0));
    }

//    @Test
//    public void updateAllNotificationsSeen(){
//        List<Notification> notifications = notificationService.findAll();
//        String token = this.obtainAccessToken("admin@stenden.com", "password");
//
//        given()
//                .auth()
//                .oauth2(token)
//                .contentType(ContentType.JSON)
//                .when()
//                .patch("/api/v1/notifications")
//                .then()
//                .statusCode(HttpStatus.OK.value());
//
//        for(Notification notification : notifications){
//            assertThat(notification.isSeen(), is(equalTo(true)));
//        }
//
//    }

    @Test
    public void updateNotification(){
        Notification testNotification = notificationService.findById(Long.parseLong("1"));
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        Map<String, Boolean> notificationData = new HashMap<>();
        notificationData.put("seen", true);

        given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(notificationData)
                .when()
                .patch("/api/v1/notifications/{notificationId}", testNotification.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("seen", is(notificationData.get("seen")));

        Notification updatedNotification = notificationService.findById(testNotification.getId());

        assertThat("Notification is marked as seen.", updatedNotification.isSeen(), not(equalTo(testNotification.isSeen())));
    }
}
