package com.javahelp.backend.endpoint;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;

/**
 * Abstract handler that deals with AWS API Gateway events with JSON or empty bodies
 *
 * @param <T> type param for response
 */
public abstract class HTTPHandler<T> implements RequestHandler<Map<String, Object>, T> {

    /**
     * Response code for unsupported media type errors
     *
     * Should be returned by NonJSONBodyResponse
     */
    protected static int UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * Response code for bad requests
     */
    protected static int BAD_REQUEST = 400;

    /**
     * Response code for internal server errors
     */
    protected static int INTERNAL_SERVER_ERROR = 500;

    @Override
    public T handleRequest(Map<String, Object> input, Context context) {
        // parse body
        JsonObject body = extractBody(input);

        // parse methods
        HttpMethod method = extractMethod(input);

        // parse headers
        Map<String, String[]> headers = extractHeaders(input);

        // parse parameters
        Map<String, String[]> parameters = extractParameters(input);

        // if the body is defined, but not a json, return a
        if (body != null && !hasJSONHeader(headers)) {
            return getNonJSONBodyResponse();
        }

        return getResponse(body, method, headers, parameters);
    }

    /**
     * @param input {@link Map} of the input
     * @return {@link JsonObject} body of the request, or null if the body is not JSON
     */
    private static JsonObject extractBody(Map<String, Object> input) {
        JsonReader reader = Json.createReader(new StringReader((String) input.get("body")));
        try {
            return reader.readObject();
        } catch (JsonParsingException e) {
            return null;
        }
    }

    /**
     * @param input {@link Map} of input
     * @return {@link HttpMethod} used to call this handler
     */
    private static HttpMethod extractMethod(Map<String, Object> input) {
        String methodS = (String) input.get("httpMethod");
        HttpMethod method = Enum.valueOf(HttpMethod.class, methodS);
        return method;
    }

    /**
     * @param input {@link Map} of input
     * @return {@link Map} of headers
     */
    private static Map<String, String[]> extractHeaders(Map<String, Object> input) {
        JsonReader headerReader = Json.createReader(new StringReader((String) input.get("headers")));
        JsonObject headerObject = headerReader.readObject();
        Map<String, String[]> headers = new HashMap<>();
        headerObject.forEach((header, value) -> headers.put(header, value.toString().split(",")));
        return headers;
    }

    /**
     * @param input {@link Map} of input
     * @return {@link Map} of parameters
     */
    private static Map<String, String[]> extractParameters(Map<String, Object> input) {
        JsonReader reader =
                Json.createReader(new StringReader((String) input.get("queryStringParameters")));
        JsonObject jsonObject = reader.readObject();
        Map<String, String[]> parameters = new HashMap<>();
        jsonObject.forEach(
                (parameter, value) -> parameters.put(parameter, value.toString().split(",")));
        return parameters;
    }

    /**
     * @param headers headers for this request
     * @return whether the content type of the body is JSON
     */
    private static boolean hasJSONHeader(Map<String, String[]> headers) {
        boolean hasJSON = headers.containsKey("Content-Type");
        if (hasJSON) {
            String[] header = headers.get("Content-Type");
            hasJSON = Arrays.asList(header).contains("application/json");
        }
        return hasJSON;
    }

    /**
     * Gets the response to the specified request
     *
     * @param body       {@link JsonObject} request body
     * @param method     {@link HttpMethod} http method called
     * @param headers    {@link Map} of {@link String} headers to {@link String} array header values
     * @param parameters {@link Map} of {@link String} parameters to {@link String}
     *                   array parameter values
     * @return response object
     */
    public abstract T getResponse(JsonObject body,
                                  HttpMethod method,
                                  Map<String, String[]> headers,
                                  Map<String, String[]> parameters);

    /**
     * @return response to send when the body is non JSON, using UNSUPPORTED_MEDIA_TYPE
     */
    public abstract T getNonJSONBodyResponse();
}
