package org.rishi.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomatePost {

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.postman.com").
                addHeader("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();


        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validate_post_request_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\":\n" +
                "        {\n" +
                "            \"name\": \"myFirstWorkspace\",\n" +
                "            \"type\": \"personal\",\n" +
                "            \"description\": \"Rest Assured created this\"\n" +
                "        }\n" +
                "}";
        given().
                body(payload).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name", equalTo("myFirstWorkspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validate_post_request_non_bdd_style(){
        String payload = "{\n" +
                "    \"workspace\":\n" +
                "        {\n" +
                "            \"name\": \"myFirstWorkspace\",\n" +
                "            \"type\": \"personal\",\n" +
                "            \"description\": \"Rest Assured created this\"\n" +
                "        }\n" +
                "}";

        Response response = with().
                body(payload).
                post("/workspaces");

        assertThat(response.<String>path("workspace.name"), equalTo("myFirstWorkspace"));
        assertThat(response.<String>path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validate_post_request_payload_from_file(){
        File file = new File("src/main/resources/CreateWorkspacePayload.json");
        given().
                body(file).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name", equalTo("new"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validate_post_request_payload_as_map(){
        HashMap<String, Object> mainObject = new HashMap<>();

        HashMap<String, String> nestedObject = new HashMap<>();
        nestedObject.put("name", "myThirdWorkspace");
        nestedObject.put("type", "personal");

        mainObject.put("workspace", nestedObject);

        given().
                body(mainObject).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name", equalTo("new1"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

}
