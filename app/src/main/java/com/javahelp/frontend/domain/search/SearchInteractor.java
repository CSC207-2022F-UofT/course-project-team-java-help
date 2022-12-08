package com.javahelp.frontend.domain.search;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.Set;

public class SearchInteractor implements ISearchInput {

    ISearchOutput output;
    ISearchDataAccess searchAccess;

    /**
     * Creates a new {@link SearchInteractor}
     *
     * @param output      {@link ISearchOutput} to output to
     * @param searchAccess  {@link ISearchDataAccess} to get salt from
     */
    public SearchInteractor(ISearchOutput output, ISearchDataAccess searchAccess) {
        this.output = output;
        this.searchAccess = searchAccess;
    }

    @Override
    public void search(String userID, Set<String> filters, boolean isRanking) {
        searchAccess.search(userID, filters, isRanking, new FutureCallback<SearchResult>() {
            @Override
            public void completed(SearchResult result) {
                if (result.isSuccess()) {
                    output.success(result.getUsers(), result.getResponses());
                } else {
                    output.failure();
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
