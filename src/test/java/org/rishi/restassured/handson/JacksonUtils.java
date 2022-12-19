package org.rishi.restassured.handson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JacksonUtils {

    public static <T> T deserializeJson1(String fileName, Class<T> T) throws IOException {
        InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, T);
    }

    public static <T> Object[][] deserializeJson2(String fileName, Class<T> T) throws IOException {
        InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> workList = objectMapper.readValue(is, new TypeReference<List<Object>>() {});
        return workList.stream()
                .map(work -> new Object[] { work })
                .toArray(Object[][]::new);
    }

  /*  public static <T> T[][] deserializeJson3(String fileName, Class<T> T[][]) throws IOException {
        InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, <T>);
    }*/
}
