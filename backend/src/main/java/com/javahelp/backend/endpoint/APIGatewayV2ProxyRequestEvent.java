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

    /**
     * Sets the request context for this request
     *
     * @param requestContext {@link ProxyRequestContext} to set
     */
    public void setRequestContext(ProxyRequestContext requestContext) {
        this.requestContext = requestContext;
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
