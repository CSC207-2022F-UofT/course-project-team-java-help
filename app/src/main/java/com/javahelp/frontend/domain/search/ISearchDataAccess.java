package com.javahelp.frontend.domain.search;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.Set;
import java.util.concurrent.Future;

/**
 * Data access interface for {@link SearchInteractor}
 */
public interface ISearchDataAccess {

    /**
     * Search for providers with the provided client ID and filters.
     *
     * @param userID {@link String} id of the client
     * @param filters {@link Set} of {@link String} filters / attributes for query constraints
     * @param isRanking {@link boolean} whether client-based ranking is called.
     * @param callback     the {@link FutureCallback} to call, or null
     *
     * @return {@link Future} with {@link SearchResult}
     */
    Future<SearchResult> search(String userID, Set<String> filters, boolean isRanking, FutureCallback<SearchResult> callback);

}
