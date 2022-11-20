package com.javahelp.backend.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Lambda function response to be sent over AWS API Gateway. Based on class provided by serverless
 * framework.
 */
public class APIGatewayResponse {

    /**
     * Response code for unsupported media type errors
     * <p>
     * Should be returned by NonJSONBodyResponse
     */
    public static int UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * Response code for bad requests
     */
    public static int BAD_REQUEST = 400;

    /**
     * Response code for internal server errors
     */
    public static int INTERNAL_SERVER_ERROR = 500;

    /**
     * Response code for OK status
     */
    public static int OK = 200;

    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final boolean isBase64Encoded;

    /**
     * Creates a new {@link APIGatewayResponse}
     *
     * @param statusCode      the status code to use in this response
     * @param body            {@link String} body to send
     * @param headers         {@link Map} of headers to send
     * @param isBase64Encoded whether the response is a base 64 encoded binary string
     */
    private APIGatewayResponse(int statusCode, String body, Map<String, String> headers, boolean isBase64Encoded) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.isBase64Encoded = isBase64Encoded;
    }

    /**
     * @return the status code for the response
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return {@link String} body
     */
    public String getBody() {
        return body;
    }

    /**
     * @return {@link Map} of response headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * API Gateway expects the property to be called "isBase64Encoded" => isIs
     *
     * @return whether this response is a base64 encoded binary response
     */
    public boolean isIsBase64Encoded() {
        return isBase64Encoded;
    }

    /**
     * @return {@link Builder} for a {@link APIGatewayResponse}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets an error {@link APIGatewayResponse}
     *
     * @param code    status code to use
     * @param message {@link String} message to use
     * @return an error {@link APIGatewayResponse}
     */
    public static APIGatewayResponse error(int code, String message) {

        JsonObject json = Json.createObjectBuilder().add("message", message).build();

        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");

        return builder()
                .setStatusCode(code)
                .setJSONBody(json.toString())
                .setHeaders(headers)
                .build();
    }

    /**
     * Inner class for constructing {@link APIGatewayResponse}s
     */
    public static class Builder {

        private static final ObjectMapper objectMapper = new ObjectMapper();

        private int statusCode = 200;
        private Map<String, String> headers = Collections.emptyMap();
        private String rawBody;
        private Object objectBody;
        private byte[] binaryBody;
        private boolean base64Encoded;

        /**
         * @param statusCode status code to response with
         * @return this {@link Builder} for chaining
         */
        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        /**
         * @param headers {@link Map} of headers to assign
         * @return this {@link Builder} for chaining
         */
        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Builds the {@link APIGatewayResponse} using the passed JSON string representation
         */
        public Builder setJSONBody(String json) {
            headers.put("Content-Type", "application/json");
            this.rawBody = json;
            return this;
        }

        /**
         * Builds the {@link APIGatewayResponse} using the passed raw body string.
         */
        public Builder setRawBody(String rawBody) {
            this.rawBody = rawBody;
            return this;
        }

        /**
         * Builds the {@link APIGatewayResponse} using the passed object body
         * converted to JSON.
         */
        public Builder setJSONBody(Object objectBody) {
            headers.put("Content-Type", "application/json");
            this.objectBody = objectBody;
            return this;
        }

        /**
         * Builds the {@link APIGatewayResponse} using the passed binary body
         * encoded as base64. {@link #setBase64Encoded(boolean)
         * setBase64Encoded(true)} will be in invoked automatically.
         */
        public Builder setBinaryBody(byte[] binaryBody) {
            this.binaryBody = binaryBody;
            setBase64Encoded(true);
            return this;
        }

        /**
         * A binary or rather a base64encoded responses requires
         * <ol>
         * <li>"Binary Media Types" to be configured in API Gateway
         * <li>a request with an "Accept" header set to one of the "Binary Media
         * Types"
         * </ol>
         */
        public Builder setBase64Encoded(boolean base64Encoded) {
            this.base64Encoded = base64Encoded;
            return this;
        }

        /**
         * @return {@link APIGatewayResponse} built by this {@link Builder}
         */
        public APIGatewayResponse build() {
            String body = null;
            if (rawBody != null) {
                body = rawBody;
            } else if (objectBody != null) {
                try {
                    body = objectMapper.writeValueAsString(objectBody);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else if (binaryBody != null) {
                body = new String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8);
            }
            return new APIGatewayResponse(statusCode, body, headers, base64Encoded);
        }
    }
}
