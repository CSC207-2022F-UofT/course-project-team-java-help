package com.javahelp.frontend.domain.user.read;

import com.javahelp.model.user.User;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * Interactor for reading {@link User} info
 */
public class ReadInteractor implements IReadInput {

    private final IReadDataAccess reader;

    private final IReadOutput output;

    /**
     * Creates a new {@link ReadInteractor}
     * @param output {@link IReadOutput} to output to
     * @param reader {@link IReadDataAccess} to use
     */
    public ReadInteractor(IReadOutput output, IReadDataAccess reader) {
        this.reader = reader;
        this.output = output;
    }

    @Override
    public void read(String id) {
        reader.read(id, new FutureCallback<User>() {
            @Override
            public void completed(User result) {
                output.success(result);
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
