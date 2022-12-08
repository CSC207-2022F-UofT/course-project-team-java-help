package com.javahelp.frontend.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.search.ISearchDataAccess;
import com.javahelp.frontend.domain.search.ISearchInput;
import com.javahelp.frontend.domain.search.ISearchOutput;
import com.javahelp.frontend.domain.search.SearchInteractor;
import com.javahelp.frontend.domain.search.SearchResult;
import com.javahelp.frontend.gateway.LambdaSearchDataAccess;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragmentVM extends AndroidViewModel implements ISearchOutput {
    private final MutableLiveData<String> userID = new MutableLiveData<>("");
    private final MutableLiveData<Set<String>> filters = new MutableLiveData<>(new HashSet<>());
    private final MutableLiveData<Boolean> isRanking = new MutableLiveData<>(Boolean.FALSE);
    private final MutableLiveData<Boolean> isFiltering = new MutableLiveData<>(Boolean.TRUE);
    private final MutableLiveData<Boolean> isSearching = new MutableLiveData<>(Boolean.FALSE);
    private final MutableLiveData<SearchResult> searchResult = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ISearchInput searchInteractor;

    /**
     * Default {@link AndroidViewModel} constructor for {@link LoginViewModel}
     *
     * @param application {@link Application} to use
     */
    public SearchFragmentVM(@NonNull Application application) {
        super(application);
        ISearchDataAccess search = new LambdaSearchDataAccess(new SharedPreferencesAuthInformationProvider(application.getBaseContext()));
        searchInteractor = new SearchInteractor(this, search);
    }

    public void search() {
        setIsFiltering(false);
        setIsSearching(true);
        executor.execute(() -> {
            searchInteractor.search(userID.getValue(),
                    filters.getValue(),
                    Boolean.TRUE.equals(isRanking.getValue()));
        });
    }

    public void setUserID(String userID) {
        this.userID.setValue(userID);
    }

    public void setFilter(String filter) {
        Set<String> filters = this.filters.getValue();
        if (filters != null && filters.contains(filter)) {
            filters.remove(filters);
            this.filters.setValue(filters);
        } else {
            filters.add(filter);
            this.filters.setValue(filters);
        }
        setIsFiltering(true);
    }

    public void setIsRanking() {
        this.isRanking.setValue(Boolean.FALSE.equals(this.isRanking.getValue()));
        setIsFiltering(true);
    }

    public void setIsFiltering(boolean filtering) {
        this.isFiltering.setValue(filtering);
    }

    public void setIsSearching(boolean searching) {
        this.isSearching.setValue(searching);
    }

    public MutableLiveData<String> getUserID() {
        return this.userID;
    }

    public MutableLiveData<Set<String>> getFilters() {
        return this.filters;
    }

    public MutableLiveData<Boolean> getIsRanking() {
        return this.isRanking;
    }

    public MutableLiveData<Boolean> getIsFiltering() {
        return this.isFiltering;
    }

    public MutableLiveData<Boolean> getIsSearching() {
        return this.isSearching;
    }

    public MutableLiveData<SearchResult> getSearchResult() {
        return searchResult;
    }

    public List<User> getUsers() {
        SearchResult result = searchResult.getValue();
        return result.getUsers();
    }

    public List<SurveyResponse> getSurveyResponses() {
        SearchResult result = searchResult.getValue();
        return result.getResponses();
    }

    /**
     * Called when the successfully retrieved list of providers from database
     *
     * @param users     {@link List} of {@link User} providers that match the client's requests.
     * @param responses {@link List} of {@link SurveyResponse}s corresponding to the {@link List}
     */
    @Override
    public void success(List<User> users, List<SurveyResponse> responses) {
        searchResult.postValue(new SearchResult(users, responses));
        isFiltering.postValue(false);
        isSearching.postValue(false);
    }

    /**
     * Called when the list of providers are not retrieved from the database.
     */
    @Override
    public void failure() {
        searchResult.postValue(new SearchResult("Failed attempt at retrieving provider info"));
        isFiltering.postValue(false);
        isSearching.postValue(false);
    }

    /**
     * Called when something goes wrong before authentication
     *
     * @param errorMessage {@link String} error message received
     */
    @Override
    public void error(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "Connection error, try again";
        }
        searchResult.postValue(new SearchResult(errorMessage));
    }

    /**
     * Abort the login attempt
     */
    @Override
    public void abort() {
    }
}

