package org.search.application;

import org.search.domain.MovieRepository;
import org.search.domain.SearchResult;

import java.io.IOException;

public class SearchService {
    private final MovieRepository movieRepository;

    public SearchService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public SearchResult search(String query) throws IOException {
        return movieRepository.searchInMovies(query);
    }
}