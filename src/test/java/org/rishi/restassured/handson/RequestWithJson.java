package org.rishi.restassured.handson;

import com.fasterxml.jackson.core.*;
import com.rest.pojo.workspace.Workspace;
import com.rest.pojo.workspace.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class RequestWithJson {

    @BeforeClass
    public void beforeClass() {
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

    @Test(dataProvider = "workspace2")
    public void workspace_serialize_deserialize(Workspace workspace) {
        //Workspace workspace = new Workspace(name, type, description);

        HashMap<String, String> myHashMap = new HashMap<>();
        workspace.setMyHashMap(myHashMap);

        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        WorkspaceRoot deserializedWorkspaceRoot = given().
                body(workspaceRoot).
        when().
                post("/workspaces").
        then().
                //spec(responseSpecification).
                        assertThat().
                extract().
                response().
                as(WorkspaceRoot.class);

        assertThat(deserializedWorkspaceRoot.getWorkspace().getName(),
                equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat(deserializedWorkspaceRoot.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @DataProvider(name = "workspace")
    public Object[][] getWorkspace() {
        return new Object[][]{
                {"myWorkspace11", "personal", "description"},
                {"myWorkspace10", "team", "description"}
        };
    }

    @DataProvider(name = "workspace1")
    public Object[][] readJson1() throws IOException {
        return JacksonUtils.deserializeJson1("payload.json", Workspace[][].class);

        //System.getProperty("user.dir")+"src/test/java/org/rishi/restassured/handson/

    }

    @DataProvider(name = "workspace2")
    public Object[][] readJson2() throws IOException {
        return JacksonUtils.deserializeJson2("payload.json", Workspace.class);

        //System.getProperty("user.dir")+"src/test/java/org/rishi/restassured/handson/

    }

}
