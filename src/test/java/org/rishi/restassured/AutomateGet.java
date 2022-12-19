package org.rishi.restassured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.restassured.config.LogConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AutomateGet {

    @Test
    void validate_status_code(){
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    void validate_response_body(){
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("workspaces.name", hasItems("My Workspace", "Team Workspace", "Team Workspace"),
                        "workspaces.type", hasItems("personal", "team", "team"),
                        "workspaces[0].name", equalTo("My Workspace"),
                        "workspaces[0].name", is(equalTo("My Workspace")),
                        "workspaces.size()", equalTo(3),
                        "workspaces.name", hasItem("Team Workspace"));
    }

    @Test
    void extract_response(){
        Response res = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                extract().
                response();

        System.out.println("response = " + res.asString());
    }

    @Test
    void extract_single_value_from_response_1(){
        Response res = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                extract().
                response();

        System.out.println("workspace name = " + res.path("workspaces[0].name"));
        System.out.println("workspace name = " + JsonPath.from(res.asString()).getString("workspaces[0].name"));
        JsonPath jsonPath = new JsonPath(res.asString());
        System.out.println("workspace name = " + jsonPath.getString("workspaces[0].name"));
    }

    @Test
    void extract_single_value_from_response_2(){
        String res = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                extract().
                response().asString();

        System.out.println("workspace name = " + JsonPath.from(res).getString("workspaces[0].name"));
        JsonPath jsonPath = new JsonPath(res);
        System.out.println("workspace name = " + jsonPath.getString("workspaces[0].name"));
    }

    @Test
    void extract_single_value_from_response_3(){
        String name = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                extract().
                response().path("workspaces[0].name");

        System.out.println("workspace name = " + name);
    }

    @Test
    void hamcrest_assert_on_extracted_response(){
        String name = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                extract().
                response().path("workspaces[0].name");
        System.out.println("workspace name = " + name);
        assertThat(name, equalTo("My Workspace")); //hamcrest
        Assert.assertEquals(name, "My Workspace"); //testNG Assertion
    }

    @Test
    void validate_response_body_hamcrest_learnings(){
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                //checks exact number of elements and the order
                body("workspaces.name", contains("My Workspace", "Team Workspace", "Team Workspace2")).
                // order doesnt matter
                body("workspaces.name", containsInAnyOrder("My Workspace", "Team Workspace2", "Team Workspace"),
                        "workspaces.name", is(not(emptyArray())),
                        "workspaces.name", hasSize(3),
//                        "workspaces.name", everyItem(startsWith("My")),
                        "workspaces[0]", hasKey("id"),
                        "workspaces[0]", hasValue("My Workspace"),
                        "workspaces[0]", hasEntry("id", "d925520c-591f-4651-8ad3-6c6a178548d7"),
                        "workspaces[0]", not(equalTo(Collections.EMPTY_MAP)),
                        "workspaces[0].name", allOf(startsWith("My"), containsString("Workspace"))
                );
    }

    @Test
    void request_response_logging(){
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
                log().headers().
        when().
                get("/workspaces").
        then().
                log().body().
                assertThat().
                statusCode(200);
    }

    @Test
    void log_only_if_error(){
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
        when().
                get("/orkspaces").
        then().
                log().ifError().
                assertThat().
                statusCode(200);
    }

    @Test
    void log_only_if_validation_fails(){
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
                config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
        when().
                get("/workspaces").
        then().
//                log().ifValidationFails().
                assertThat().
                statusCode(201);
    }

    @Test
    void log_blacklist_header(){
        Set<String> headers = new HashSet<>();
        headers.add("X-Api-Key");
        headers.add("Accept");
        given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
//                config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-Key"))).
                config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))).
                log().all().
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200);
    }





}
