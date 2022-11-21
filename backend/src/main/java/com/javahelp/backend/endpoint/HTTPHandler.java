package com.javahelp.backend.endpoint;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.UNSUPPORTED_MEDIA_TYPE;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;

/**
 * Abstract handler that deals with AWS API Gateway events with JSON or empty bodies
 */
public abstract class HTTPHandler implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayResponse> {

    @Override
    public APIGatewayResponse handleRequest(APIGatewayV2ProxyRequestEvent input, Context context) {

        // parse headers
        Map<String, String[]> headers = extractHeaders(input);

        // if the body is defined, but not a json, return a
        if (input.getBody() != null && !hasJSONHeader(headers)) {
            return APIGatewayResponse.error(UNSUPPORTED_MEDIA_TYPE, "Request body must be json");
        }

        // parse body
        JsonObject body;
        try {
            body = extractBody(input);
        } catch (JsonParsingException e) {
            return APIGatewayResponse.error(BAD_REQUEST, "Cannot parse body json");
        }

        if (requiresBody()) {
            if (body == null) {
                return APIGatewayResponse.error(BAD_REQUEST, "Missing body");
            }

            Optional<String> missing = Arrays.stream(requiredBodyFields())
                    .filter(required -> !body.containsKey(required))
                    .reduce((x, y) -> x + ", " + y);

            if (missing.isPresent()) {
                return APIGatewayResponse.error(BAD_REQUEST, "Body missing " + missing.get() + " fields");
            }
        }

        // parse methods
        HttpMethod method = extractMethod(input);

        // parse parameters
        Map<String, String[]> parameters = extractParameters(input);

        // parse path parameters
        Map<String, String> pathParameters = extractPathParameters(input);

        return getResponse(body, method, headers, parameters, pathParameters);
    }

    /**
     * @param input {@link APIGatewayV2ProxyRequestEvent} input
     * @return {@link JsonObject} body of the request, or null if the body is not JSON
     */
    private static JsonObject extractBody(APIGatewayV2ProxyRequestEvent input) {
        if (input.getBody() == null) {
            return null;
        }
        JsonReader reader = Json.createReader(new StringReader(input.getBody()));
        return reader.readObject();
    }

    /**
     * @param input {@link APIGatewayV2ProxyRequestEvent} input
     * @return {@link HttpMethod} used to call this handler
     */
    private static HttpMethod extractMethod(APIGatewayV2ProxyRequestEvent input) {
        Map<String, String> http = input.getRequestContext().getHttp();
        String methodS = http.get("method");
        return Enum.valueOf(HttpMethod.class, methodS);
    }

    /**
     * @param input {@link APIGatewayV2ProxyRequestEvent} input
     * @return {@link Map} of headers
     */
    private static Map<String, String[]> extractHeaders(APIGatewayV2ProxyRequestEvent input) {
        Map<String, String[]> headers = new HashMap<>();
        if (input.getHeaders() != null) {
            input.getHeaders().forEach((header, value) -> headers.put(header, value.split(",")));
        }
        return headers;
    }

    /**
     * @param input {@link APIGatewayV2ProxyRequestEvent} input
     * @return {@link Map} of path parameters
     */
    private static Map<String, String> extractPathParameters(APIGatewayV2ProxyRequestEvent input) {
        Map<String, String> params = new HashMap<>();
        if (input.getPathParameters() != null) {
            params.putAll(input.getPathParameters());
        }
        return params;
    }

    /**
     * @param input {@link APIGatewayV2ProxyRequestEvent} of input
     * @return {@link Map} of parameters
     */
    private static Map<String, String[]> extractParameters(APIGatewayV2ProxyRequestEvent input) {
        Map<String, String[]> parameters = new HashMap<>();
        if (input.getQueryStringParameters() != null) {
            input.getQueryStringParameters().forEach((param, value) -> parameters.put(param, value.split(",")));
        }
        return parameters;
    }

    /**
     * @param headers headers for this request
     * @return whether the content type of the body is JSON
     */
    private static boolean hasJSONHeader(Map<String, String[]> headers) {
        boolean hasJSON = headers.containsKey("content-type");
        if (hasJSON) {
            hasJSON = false;
            String[] header = headers.get("content-type");
            for (String value : header) {
                hasJSON = hasJSON || "application/json".equals(value);
            }
        }
        return hasJSON;
    }

    /**
     * @return whether this handler requires a body
     */
    public boolean requiresBody() {
        return true;
    }

    /**
     * Will only be checked if body is required
     *
     * @return the fields required in the body
     */
    public String[] requiredBodyFields() {
        return new String[0];
    }

    /**
     * Gets the response to the specified request
     *
     * @param body           {@link JsonObject} request body
     * @param method         {@link HttpMethod} http method called
     * @param headers        {@link Map} of {@link String} headers to {@link String} array header values
     * @param parameters     {@link Map} of {@link String} parameters to {@link String}
     *                       array parameter values
     * @param pathParameters {@link Map} of {@link String} path parameter names to {@link String} values
     * @return response object
     */
    public abstract APIGatewayResponse getResponse(JsonObject body,
                                                   HttpMethod method,
                                                   Map<String, String[]> headers,
                                                   Map<String, String[]> parameters,
                                                   Map<String, String> pathParameters);

}
