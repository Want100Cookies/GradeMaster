package com.datbois.grademaster;

import com.datbois.grademaster.model.Notification;
import com.datbois.grademaster.service.NotificationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

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

    @Test
    public void getNotification(){
        String token = this.obtainAccessToken("admin@stenden.com", "password");

        Notification notification = notificationService.findById(new Long(1));

        given()
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/notifications/" + notification.getId())
                .then()
                .body("size()", greaterThan(0));
    }
}
