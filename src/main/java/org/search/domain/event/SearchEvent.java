package org.search.domain.event;

import org.search.domain.model.Query;
import java.util.logging.Logger;

public class SearchEvent {
    private final Query query;
    private final int resultCount;
    private static final Logger logger = Logger.getLogger(SearchEvent.class.getName());

    public SearchEvent(Query query, int resultCount) {
        this.query = query;
        this.resultCount = resultCount;
    }

    public Query getQuery() {
        return query;
    }

    public int getResultCount() {
        return resultCount;
    }

    @Override
    public String toString() {
        return String.format("SearchEvent[query=%s, resultCount=%d]",
                query.getValue(), resultCount);
    }
}