package com.javahelp.frontend.domain.search;

import java.util.Set;

public interface ISearchInput {

    /**
     * Search for roviders with the specified client-id (for ranking, if necessary),
     * set of filters selected, and/or a boolean which toggles if ranking based on the specific
     * client's personal info is called.
     * Should output results through a {@link ISearchOutput}.
     *
     * @param userID {@link String} id of the client
     * @param filters {@link Set} of {@link String} filters / attributes for query constraints
     * @param isRanking {@link boolean} whether client-based ranking is called.
     */
    void search(String userID, Set<String> filters, boolean isRanking);
}
