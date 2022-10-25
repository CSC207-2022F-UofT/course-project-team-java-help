package com.javahelp.backend;

/**
 * Wrapper around a message string to be used as an AWS Lambda response
 */
public class SimpleResponse {

    private final String message;

    /**
     * Creates a new {@link SimpleResponse}
     * 
     * @param s message for this {@link SimpleResponse}
     */
    public SimpleResponse(String s) {
        message = s;
    }

    /**
     * 
     * @return the {@link String} message in this {@link SimpleResponse}
     */
    public String getMessage() {
        return message;
    }

}
