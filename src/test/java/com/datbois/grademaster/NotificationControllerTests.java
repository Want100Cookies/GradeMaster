package com.datbois.grademaster;

import com.datbois.grademaster.service.NotificationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

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
                .get("/api/v1/notification/1")
                .then()
                .body("size()", greaterThan(0));
    }
}
