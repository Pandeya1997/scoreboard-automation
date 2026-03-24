package com.scoreboard.util;

import com.scoreboard.constants.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APICallUtil {

    private static String token;

    public String loginAndGetToken(String username, String password) {

        if (token != null) {
            return token;
        }

        Response response =
                given()
                        .baseUri(ApiEndpoints.BASE_URL)
                        .header("Content-Type", "application/json")
                        .body("{\n" +
                                "  \"cust_user_id\": \"" + username + "\",\n" +
                                "  \"passwd\": \"" + password + "\"\n" +
                                "}")
                        .when()
                        .post(ApiEndpoints.LOGIN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        token = response.jsonPath().getString("data.accessToken");

        return token;
    }

    public Response getCountries() {

        String accessToken = loginAndGetToken(TestBase.getUsername(), TestBase.getPassword());

        return given()
                .baseUri(ApiEndpoints.BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get(ApiEndpoints.GET_COUNTRY)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public List<String> getAllActiveManagers() {

        List<String> managerNames = new ArrayList<>();

        int page = 1;
        String accessToken = loginAndGetToken(TestBase.getUsername(), TestBase.getPassword());
        while (true) {

            Response response =
                    RestAssured.given()
                            .baseUri(ApiEndpoints.BASE_URL)
                            .queryParam("role", "MANAGER")
                            .queryParam("name", "")
                            .queryParam("page", page)
                            .queryParam("limit", 50)
                            .header("Authorization", "Bearer " + accessToken)
                            .when()
                            .get(ApiEndpoints.MANAGER);

            Integer totalPages = response.jsonPath().get("pagination.totalPages");

            List<Map<String, Object>> managers = response.jsonPath().getList("data");

            for (Map<String, Object> manager : managers) {

                Integer status = (Integer) manager.get("status");

                if (status != null && status == 1) {
                    managerNames.add(manager.get("name").toString());
                }
            }

            if (page >= totalPages) {
                break;
            }

            page++;
        }

        return managerNames;
    }
}


