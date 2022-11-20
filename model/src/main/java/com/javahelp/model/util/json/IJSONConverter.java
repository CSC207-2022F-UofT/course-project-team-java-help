package com.javahelp.model.util.json;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;

/**
 * Interface for converting an type to and from JSON
 * @param <T> the type to convert
 */
public interface IJSONConverter<T> {

    /**
     * Converts the provided object to a {@link JsonObject}
     * @param input input object
     * @return {@link JsonObject} or null if conversion failed
     */
    JsonObject toJSON(T input);

    /**
     * Converts the specified object to a JSON {@link String}
     * @param input input object
     * @return JSON {@link String} or null if conversion failed
     */
    default String toJSONString(T input) {
        JsonObject object = toJSON(input);
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    /**
     * Reads from a {@link String}
     * @param jsonString {@link String} to read from
     * @return output object or null if cannot convert
     */
    default T fromJSONString(String jsonString) {
        StringReader reader = new StringReader(jsonString);
        JsonReader object = Json.createReader(reader);
        try {
            return fromJSON(object.readObject());
        } catch (JsonParsingException e) {
            return null;
        }
    }

    /**
     * Converts a {@link JsonObject} to an output
     * @param object {@link JsonObject} to convert
     * @return output object, or null if conversion failed
     */
    T fromJSON(JsonObject object);

}
