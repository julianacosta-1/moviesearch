package org.search.domain;

import java.io.IOException;

public interface MovieRepository {
    SearchResult searchInMovies(Query query);
}