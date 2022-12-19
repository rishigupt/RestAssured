package org.rishi.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.get;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ResponseSpecificationExample {
    ResponseSpecification responseSpecification;

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

        RestAssured.requestSpecification = requestSpecBuilder.build();

       /* responseSpecification = RestAssured.expect().
                statusCode(200).
                contentType(ContentType.JSON).
                log().all();*/

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);

        responseSpecification = responseSpecBuilder.build();

    }

    @Test
    void validate_status_code(){
       get("/workspaces").
       then().spec(responseSpecification);
    }

    @Test
    void validate_response_body(){
        Response response = get("/workspaces").
                then().spec(responseSpecification).
                        extract().
                        response();
        assertThat(response.path("workspaces[0].name").toString(), equalTo("My Workspace"));
    }
}
