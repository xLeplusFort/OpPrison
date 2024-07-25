package ru.snowyk.opprison.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class JsonUtil {
    public static final Gson GSON = new Gson();
    public static final JsonParser JSON_PARSER = new JsonParser();

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static JsonElement parse(String json) {
        if (json == null) {
            throw new NullPointerException("json is marked non-null but is null");
        } else {
            return JSON_PARSER.parse(json);
        }
    }

    private JsonUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
