package org.search.domain.repository;

import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;

public interface MovieRepository {
    SearchResult searchInMovies(Query query);
}