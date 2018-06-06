package com.ibm.cdays.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JsonUtil {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static final ObjectMapper JSON_MAPPER_PRETTY = new ObjectMapper();

    static {
        JSON_MAPPER_PRETTY.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private JsonUtil() {

    }

    public static String serializeToJson(Object object) {
        return serializeToJson(object, false);
    }

    public static String serializeToJson(Object object, boolean prettyPrint) {
        try {
            if (prettyPrint) {
                return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                return JSON_MAPPER.writeValueAsString(object);
            }
        } catch (Exception throwable) {
            throw new RuntimeException(throwable);
        }
    }


}
