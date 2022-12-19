package org.rishi.restassured;

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

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class WorkspacePojoTest {

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
    public void validate_post_request_payload_as_map(){
        Workspace workspace = new Workspace("myWorkspace6","personal", "description");
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        WorkspaceRoot deserializedWorkspaceRoot = given().
                body(workspaceRoot).
        when().
                post("/workspaces").
        then().spec(responseSpecification).
                assertThat().
                extract().
                response().
                as(WorkspaceRoot.class);

        assertThat(deserializedWorkspaceRoot.getWorkspace().getName(),
                equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat(deserializedWorkspaceRoot.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test (dataProvider = "workspace")
    public void workspace_serialize_deserialize_using_constructor(String name, String type, String description){
        Workspace workspace = new Workspace(name, type, description);
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

    @Test (dataProvider = "workspace")
    public void workspace_serialize_deserialize_using_setter(HashMap<String, String> myHashMap){
        Workspace workspace = new Workspace();
        /*HashMap<String, String> myHashMap = new HashMap<>();
        myHashMap.put("name", name);
        myHashMap.put("type",type);
        myHashMap.put("description", description);*/
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
    public Object[][] getWorkspace(){
        return new Object[][]{
                {"myWorkspace18", "personal", "description"},
                //{"myWorkspace", "team", "description"}
        };
    }



















}
