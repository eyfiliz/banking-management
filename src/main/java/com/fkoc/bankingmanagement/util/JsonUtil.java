package com.fkoc.bankingmanagement.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String objectToJson(Object jsonObject) {

        String json;

        if (jsonObject == null) {
            json = "Null Object";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            try {
                json = mapper.writeValueAsString(jsonObject);
            } catch (Exception e) {
                json = "Object could not be converted to Json Format";
            }
        }
        return json;
    }
}