package org.search.application;

import org.search.domain.InvalidQueryException;
import org.search.domain.MovieRepository;
import org.search.domain.Query;
import org.search.domain.SearchResult;

public class SearchService {
    private final MovieRepository movieRepository;

    public SearchService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

//    public SearchResult search(String query) {
//        if (query == null || query.trim().isEmpty()) {
//            throw new InvalidQueryException("Search query cannot be null or empty.");
//        }
//        return movieRepository.searchInMovies(query);  // Custom exceptions will be handled here
//    }

    public SearchResult search(Query query) {
        if (query == null) {
            throw new InvalidQueryException("Search query cannot be null.");
        }
        return movieRepository.searchInMovies(query);
    }
}