package com.javahelp.frontend.domain.user.delete;

import com.javahelp.model.user.User;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * The frontend interactor used for deleting a {@link User}.
 */
public class DeleteInteractor implements IDeleteInput {

    private final IDeleteOutput output;
    private final IDeleteDataAccess dataAccess;

    /**
     * Constructs a {@link DeleteInteractor} instance.
     * @param output: the {@link IDeleteOutput} output boundary to use.
     * @param dataAccess: the {@link IDeleteDataAccess} data access interface to use.
     */
    public DeleteInteractor(IDeleteOutput output, IDeleteDataAccess dataAccess) {
        this.output = output;
        this.dataAccess = dataAccess;
    }

    @Override
    public void delete(String userID) {
        dataAccess.delete(userID, new FutureCallback<DeleteResult>() {
            @Override
            public void completed(DeleteResult result) {
                if (result.isSuccess()) {
                    output.success(result.getUser());
                }
                else {
                    output.failure(result.getErrorMessage());
                }
            }

            @Override
            public void failed(Exception ex) {
                output.error(ex.getMessage());
            }

            @Override
            public void cancelled() {
                output.abort();
            }
        });
    }
}
