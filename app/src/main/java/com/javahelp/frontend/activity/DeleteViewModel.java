package com.javahelp.frontend.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.delete.DeleteInteractor;
import com.javahelp.frontend.domain.user.delete.DeleteResult;
import com.javahelp.frontend.domain.user.delete.IDeleteDataAccess;
import com.javahelp.frontend.domain.user.delete.IDeleteInput;
import com.javahelp.frontend.domain.user.delete.IDeleteOutput;
import com.javahelp.frontend.gateway.IAuthInformationProvider;
import com.javahelp.frontend.gateway.LambdaDeleteDataAccess;
import com.javahelp.model.user.User;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@link AndroidViewModel} for {@link DeleteActivity}.
 */
public class DeleteViewModel extends AndroidViewModel implements IDeleteOutput {

    private final IDeleteInput deleteInteractor;
    private final IDeleteDataAccess dataAccess;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Optional<DeleteResult>> deleteResult = new MutableLiveData<>(Optional.empty());
    private final MutableLiveData<Optional<Boolean>> deleting = new MutableLiveData<>(Optional.of(false));

    /**
     * Constructs a {@link DeleteViewModel}.
     * @param application: the {@link Application} to use.
     * @param provider: the {@link IAuthInformationProvider} that provides information
     *                of the {@link User} to be deleted.
     */
    public DeleteViewModel(@NonNull Application application, IAuthInformationProvider provider) {
        super(application);
        dataAccess = new LambdaDeleteDataAccess(provider);
        deleteInteractor = new DeleteInteractor(this, dataAccess);
    }

    /**
     * Sets an error occurred in the delete process.
     * @param errorMessage: the {@link String} error message to be received.
     */
    public void setDeleteError(String errorMessage) {
        deleteResult.setValue(Optional.of(new DeleteResult(errorMessage)));
    }

    /**
     * @return the {@link DeleteResult} of this deletion.
     */
    public MutableLiveData<Optional<DeleteResult>> getDeleteResult() {
        return deleteResult;
    }

    /**
     * @return whether there is a deletion attempt running.
     */
    public MutableLiveData<Optional<Boolean>> isDeleting() {
        return deleting;
    }

    /**
     * Sets the status of this deletion.
     * @param b: whether this deletion attempt is running.
     */
    public void setDeleting(boolean b) {
        deleting.setValue(Optional.of(b));
    }

    @Override
    public void success(User user) {
        deleting.postValue(Optional.of(false));
        deleteResult.postValue(Optional.of(new DeleteResult(user)));
    }

    @Override
    public void failure(String errorMessage) {
        deleting.postValue(Optional.of(false));
        deleteResult.postValue(Optional.of(new DeleteResult(errorMessage)));
    }

    @Override
    public void error(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "Connection error, try again";
        }
        deleting.postValue(Optional.of(false));
        deleteResult.postValue(Optional.of(new DeleteResult(errorMessage)));
    }

    @Override
    public void abort() {
        deleting.postValue(Optional.of(false));
        deleteResult.postValue(Optional.empty());
    }

    /**
     * Attempts to delete the {@link User} provided by the {@link IAuthInformationProvider}.
     * @param provider: the {@link IAuthInformationProvider} that provides information
     *                of the {@link User} to be deleted.
     */
    public void attemptDelete(IAuthInformationProvider provider) {
        setDeleting(true);
        executor.execute(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {

            }
            deleteInteractor.delete(provider.getUserID());
        });
    }

}
