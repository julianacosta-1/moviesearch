package org.search.application;

import org.search.domain.event.SearchEvent;
import org.search.domain.event.SearchEventListener;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private final MovieRepository movieRepository;
    private final List<SearchEventListener> listeners = new ArrayList<>();

    public SearchService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void addSearchEventListener(SearchEventListener listener) {
        listeners.add(listener);
    }

    public List<SearchEventListener> getListeners() {
        return listeners;
    }

    public SearchResult search(Query query) {
        SearchResult result = movieRepository.searchInMovies(query);

        // Create and fire the search event
        SearchEvent event = new SearchEvent(query, result.getOccurrenceCount());
        fireSearchCompletedEvent(event);

        return result;
    }

    private void fireSearchCompletedEvent(SearchEvent event) {
        for (SearchEventListener listener : listeners) {
            listener.onSearchCompleted(event);
        }
    }
}