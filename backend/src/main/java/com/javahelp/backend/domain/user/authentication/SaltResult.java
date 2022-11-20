package com.javahelp.backend.domain.user.authentication;

/**
 * Result from a {@link SaltInteractor}
 */
public class SaltResult {

    private String errorMessage = null;

    private byte[] salt = null;

    /**
     * Creates a new {@link SaltResult}
     *
     * @param salt byte array with salt
     */
    SaltResult(byte[] salt) {
        this.salt = salt;
    }

    /**
     * Creates a new {@link SaltResult}
     *
     * @param errorMessage {@link String} error message
     */
    SaltResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return whether the result is a success
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * @return the error {@link String} if the result is a failure
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return the retrieved salt
     */
    public byte[] getSalt() {
        return salt;
    }

}
