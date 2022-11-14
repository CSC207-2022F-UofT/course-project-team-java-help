package com.javahelp.backend.accountdeletion;

import com.javahelp.model.user.UserPassword;

/**
 * A class for testing {@link IDeleteInputBoundary}.
 * This class will be deleted later, once we have a class implementing {@link IDeleteInputBoundary}.
 */
class DeleteInputBoundaryImplementation implements IDeleteInputBoundary{
    private final String userID;
    private final UserPassword userPassword;

    public DeleteInputBoundaryImplementation(String userID, UserPassword userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
    }

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public UserPassword getUserPassword() {
        return userPassword;
    }
}
