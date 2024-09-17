package org.search.domain;

public class SearchEvent {
    private final Query query;       // The search query
    private final int resultCount;   // Number of occurrences found
    private final long elapsedTime;  // Time taken to complete the search

    public SearchEvent(Query query, int resultCount, long elapsedTime) {
        this.query = query;
        this.resultCount = resultCount;
        this.elapsedTime = elapsedTime;
    }

    public Query getQuery() {
        return query;
    }

    public int getResultCount() {
        return resultCount;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String toString() {
        return String.format("SearchEvent[query=%s, resultCount=%d, elapsedTime=%d microseconds]",
                query.getValue(), resultCount, elapsedTime);
    }
}