package org.rishi.restassured;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

public class DownloadFile {

    @Test
    public void download_file() throws IOException {

        InputStream is = given().
                baseUri("https://raw.githubusercontent.com").
                log().all().
        when().
                post("/appium-boneyard/sample-code/master/sample-code/apps/ApiDemos/bin/ApiDemos-debug.apk").
        then().
                log().all().
                extract().
                response().asInputStream();

        OutputStream os = new FileOutputStream(new File("ApiDemos-debug.apk"));
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        os.write(bytes);
        os.close();
    }

    @Test
    public void download_file1() throws IOException {

        InputStream is = given().
                baseUri("https://raw.githubusercontent.com").
                log().all().
                when().
                post("/appium-boneyard/sample-code/master/sample-code/apps/ApiDemos/bin/ApiDemos-debug.apk").
                then().
                log().all().
                extract().
                response().asInputStream();

        OutputStream os = Files.newOutputStream(new File("ApiDemos-debug.apk").toPath());
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        os.write(bytes);
        os.close();
    }
}
