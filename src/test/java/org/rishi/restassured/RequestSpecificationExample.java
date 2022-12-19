package org.rishi.restassured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import io.restassured.filter.log.LogDetail;

public class RequestSpecificationExample {
    RequestSpecification requestSpecification;

    @BeforeClass
    public void beforeClass(){
        /*requestSpecification = with().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
                log().all();*/

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                        setBaseUri("https://api.postman.com").
                        addHeader("X-Api-Key", "PMAK-63240a065f570d7bec8b181c-9ca2599d0bda93a4992e98c7bf957e76c5").
                        log(LogDetail.ALL);

        requestSpecification = requestSpecBuilder.build();
    }

    @Test
    void validate_status_code(){

       /*Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();*/

        Response response = given(requestSpecification).
                header("dummyHeader", "dummyValue").
                get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));

    }

    @Test
    void validate_response_body(){

        /*Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();*/

        Response response = given(requestSpecification).get("/workspaces").then().log().all().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces[0].name").toString(), equalTo("My Workspace"));
    }
}
