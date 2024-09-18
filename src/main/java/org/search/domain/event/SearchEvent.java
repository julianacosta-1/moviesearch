package org.search.domain.event;

import org.search.domain.model.Query;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchEvent {
    private final Query query;
    private final int resultCount;
    private final long elapsedTime;
    private static final Logger logger = Logger.getLogger(SearchEvent.class.getName());

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

    public void logEvent() {
        logger.log(Level.INFO, String.format("SearchEvent[query=%s, resultCount=%d, elapsedTime=%d microseconds]",
                query.getValue(), resultCount, elapsedTime));
    }

    @Override
    public String toString() {
        return String.format("SearchEvent[query=%s, resultCount=%d, elapsedTime=%d microseconds]",
                query.getValue(), resultCount, elapsedTime);
    }
}