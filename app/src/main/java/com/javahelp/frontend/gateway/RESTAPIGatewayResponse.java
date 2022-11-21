package com.javahelp.frontend.gateway;

/**
 * Response around an object from a {@link RESTAPIGateway<T>}
 *
 * @param <T> type of object to wrap around
 */
public class RESTAPIGatewayResponse<T> {

    private T t;

    private String errorMessage;

    /**
     * Creates a new successful {@link RESTAPIGatewayResponse}
     *
     * @param t object to wrap around
     */
    RESTAPIGatewayResponse(T t) {
        this.t = t;
    }

    /**
     * Creates a new failed {@link RESTAPIGatewayResponse}
     *
     * @param errorMessage {@link String} error message
     */
    RESTAPIGatewayResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return whether the expected response was received.
     * I.E. whether the request successfully returned the desired item
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * @return error message on this response
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return the content of this {@link RESTAPIGatewayResponse}
     */
    public T get() {
        return t;
    }

}
