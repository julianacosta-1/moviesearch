package org.search.domain;

import java.io.IOException;

public interface MovieRepository {
    SearchResult searchInMovies(String query) throws IOException;
}