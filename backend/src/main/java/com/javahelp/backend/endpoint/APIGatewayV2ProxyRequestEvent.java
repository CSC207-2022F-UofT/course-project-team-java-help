package com.javahelp.backend.endpoint;

import java.io.Serializable;
import java.util.Map;

/**
 * Bundle for accepting API gateway events with Lambda.
 * <p>
 * Similar to APIGatewayProxyRequestEvent class, but compatible with V2 events.
 */
public class APIGatewayV2ProxyRequestEvent implements Serializable {

    private ProxyRequestContext requestContext;

    private String body;

    private Map<String, String> headers;

    private Map<String, String> queryStringParameters;

    private Map<String, String> pathParameters;

    private String rawPath;

    /**
     * Sets the path parameters for this request
     *
     * @param pathParameters {@link Map} of path parameters
     */
    public void setPathParameters(Map<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * Sets the request context for this request
     *
     * @param requestContext {@link ProxyRequestContext} to set
     */
    public void setRequestContext(ProxyRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Sets the raw path for this request
     *
     * @param rawPath {@link String} raw path to assign
     */
    public void setRawPath(String rawPath) {
        this.rawPath = rawPath;
    }

    /**
     * Sets the headers for this request
     *
     * @param headers {@link Map} of headers to assign
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Sets the query string for this request
     *
     * @param queryStringParameters {@link Map} of query string parameters to assign
     */
    public void setQueryStringParameters(Map<String, String> queryStringParameters) {
        this.queryStringParameters = queryStringParameters;

    }

    /**
     * Sets the body for this request
     *
     * @param body {@link String} body to assign
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return {@link ProxyRequestContext} with the context
     */
    public ProxyRequestContext getRequestContext() {
        return requestContext;
    }

    /**
     * @return {@link String} body for this request
     */
    public String getBody() {
        return body;
    }

    /**
     * @return {@link Map} of headers for this request
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return {@link Map} with query string parameters for this request
     */
    public Map<String, String> getQueryStringParameters() {
        return queryStringParameters;
    }

    /**
     * @return {@link Map} of path parameters
     */
    public Map<String, String> getPathParameters() {
        return pathParameters;
    }

    /**
     * @return the {@link String} raw path for this request
     */
    public String getRawPath() {
        return rawPath;
    }

    /**
     * Class for deserializing request context
     */
    public static class ProxyRequestContext implements Serializable {

        private Map<String, String> http;

        /**
         * Sets the HTTP for this context
         *
         * @param http {@link Map} of HTTP info to assign
         */
        public void setHttp(Map<String, String> http) {
            this.http = http;
        }

        /**
         * @return a {@link Map} of HTTP info
         */
        public Map<String, String> getHttp() {
            return http;
        }

    }

}
