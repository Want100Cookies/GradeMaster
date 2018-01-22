package com.datbois.grademaster;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test_data.sql")
public abstract class OAuthTests {

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    public String obtainAccessToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", "grademaster-client");
        params.put("client_secret", "grademaster-secret");
        params.put("scope", "read write");
        params.put("username", email);
        params.put("password", password);

        final Response response = RestAssured.given()
                .auth()
                .preemptive()
                .basic("grademaster-client", "grademaster-secret")
                .and()
                .with()
                .params(params)
                .when()
                .post("/oauth/token");

        return response.jsonPath().getString("access_token");
    }
}
