package org.rishi.restassured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.pojo.collection.*;
import com.rest.pojo.workspace.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;

public class ComplexPojoTest {

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
    public void complex_pojo_create_collection() throws JsonProcessingException, JSONException {
        Header header = new Header("Content-Type", "application/json");
        List<Header> headerList = new ArrayList<>();
        headerList.add(header);

        Body body = new Body("raw", "{\"data\": \"123\"}");

        Request request = new Request("https://postman-echo.com/post", "POST", headerList, body,
                "This is a sample POST Request");

        RequestRoot requestRoot = new RequestRoot("Sample POST Request", request);
        List<RequestRoot> requestRootList =  new ArrayList<>();
        requestRootList.add(requestRoot);

        Folder folder = new Folder("This is a folder", requestRootList);
        List<Folder> folderList = new ArrayList<>();
        folderList.add(folder);

        Info info = new Info("Sample Collection1", "This is just a sample collection.",
                "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        Collection collection = new Collection(info, folderList);
        CollectionRoot collectionRoot = new CollectionRoot(collection);



        String collectionUid = given().
                body(collectionRoot).
        when().
                post("/collections").
        then().
                //spec(responseSpecification).
                extract().
                response().path("collection.uid");


        CollectionRoot deserializedCollectionRoot = given().
                pathParam("collectionUid", collectionUid).
        when()
                .get("/collections/{collectionUid}").
        then().
                //spec(responseSpecification).
                extract().
                response().
                as(CollectionRoot.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot);
        String deserializedCollectionRootStr = objectMapper.writeValueAsString(deserializedCollectionRoot);

        JSONAssert.assertEquals(collectionRootStr, deserializedCollectionRootStr,
                new CustomComparator(JSONCompareMode.STRICT_ORDER,
                        new Customization("collection.item[*].item[*].request.url", new ValueMatcher<Object>() {
                            public boolean equal(Object o1, Object o2) {
                                return true;
                            }
                        })));

    }



}
