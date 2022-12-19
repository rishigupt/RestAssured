package org.rishi.restassured;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomateHeaders {

    @Test
    void multiple_headers(){
        Header matchHeader = new Header("x-mock-match-request-headers", "headerName");
        given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
                header("headerName", "value2").
                header(matchHeader).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    void multiple_headers_using_Headers(){
        Header header = new Header("headerName", "value2");
        Header matchHeader = new Header("x-mock-match-request-headers", "headerName");
        Headers headers = new Headers(header, matchHeader);
        given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    void multiple_headers_using_map(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value2");
        headers.put("x-mock-match-request-headers", "headerName");
        given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    void multi_value_header_in_the_request(){
         Header header1 = new Header("multiValueHeader", "value1");
         Header header2 = new Header("multiValueHeader", "value2");

         Headers headers = new Headers(header1, header2);


        given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
//                headers(headers).
//                header("multiValueHeader", "value1", "value2").
                headers(headers) .
                log().headers().
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }


    @Test
    void assert_response_headers(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value1");
        headers.put("x-mock-match-request-headers", "headerName");
        given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                headers("responseHeader", "resValue1",
                        "Server", "nginx");
    }

    @Test
    void extract_response_headers(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value1");
        headers.put("x-mock-match-request-headers", "headerName");

        Headers extractedHeaders = given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                extract().
                headers();

        for (Header header: extractedHeaders){
            System.out.println(header.getName() + ": " + header.getValue());
        }

      /*  System.out.println("header " + extractedHeaders.get("responseHeader"));
        System.out.println("header " + extractedHeaders.get("responseHeader").getName());
        System.out.println("header " + extractedHeaders.getValue("responseHeader"));*/
    }

    @Test
    void extract_multivalue_response_headers(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("headerName", "value1");
        headers.put("x-mock-match-request-headers", "headerName");

        Headers extractedHeaders = given().
                baseUri("https://5f13d57c-b8f0-4460-a87e-35e38cfaabd2.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().
                statusCode(200).
                extract().
                headers();

        List<String> values = extractedHeaders.getValues("multiValueHeader");
        System.out.println(values);

    }


}
