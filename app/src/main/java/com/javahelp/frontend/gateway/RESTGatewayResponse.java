package com.javahelp.frontend.gateway;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParsingException;

/**
 * Response from a {@link RESTAPIGateway},
 * bundles a response code and a {@link jakarta.json.JsonObject} body
 */
class RESTGatewayResponse {

    private boolean successfullyParsed;
    private boolean shouldHaveBody;
    private JsonObject body;

    /**
     * Creates a new {@link RESTGatewayResponse}
     *
     * @param body         {@link String} body to use
     * @param responseCode response code received
     */
    public RESTGatewayResponse(String body, int responseCode) {
        successfullyParsed = true;
        shouldHaveBody = body != null;
        try {
            if (shouldHaveBody) {
                this.body = Json.createReader(new StringReader(body)).readObject();
            }
        } catch (JsonParsingException e) {
            successfullyParsed = false;
        }
    }

    /**
     * @return whether the response successfully parsed every body {@link String} it was passed
     */
    public boolean isSuccessfullyParsed() {
        return successfullyParsed;
    }

    /**
     * @return whether the response was passed a body {@link String}
     */
    public boolean shouldHaveBody() {
        return shouldHaveBody;
    }

    /**
     * @return whether the response is valid (i.e. if it should have a body, was it parsed?)
     */
    public boolean validResponse() {
        return !shouldHaveBody || successfullyParsed;
    }

    /**
     * @return the response body as a {@link JsonObject}
     */
    public JsonObject getBody() {
        return body;
    }
}
