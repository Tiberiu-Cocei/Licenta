package com.android.android.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonConverter {

    public static String objectToJson(Object object) {
        try {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return objectWriter.writeValueAsString(object);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void jsonToObject(T object, String json) {
        //TODO: json to object with jackson
    }

}
