package org.search.application;

import org.search.domain.event.SearchEvent;
import org.search.domain.event.SearchEventListener;
import org.search.domain.exception.InvalidQueryException;
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

    public void removeSearchEventListener(SearchEventListener listener) {
        listeners.remove(listener);
    }

    public SearchResult search(Query query) {
        if (query == null) {
            throw new InvalidQueryException("Search query cannot be null.");
        }

        // Perform the search
        long startTime = System.nanoTime();
        SearchResult result = movieRepository.searchInMovies(query);
        long endTime = System.nanoTime();

        // Calculate elapsed time in microseconds
        long elapsedTimeMicros = (endTime - startTime) / 1000;

        // Create and fire the search event
        SearchEvent event = new SearchEvent(query, result.getOccurrenceCount(), elapsedTimeMicros);
        fireSearchCompletedEvent(event);

        return result;
    }

    private void fireSearchCompletedEvent(SearchEvent event) {
        if (listeners.isEmpty()) {
            System.out.println("No listeners are registered.");
        } else {
            System.out.println("Notifying " + listeners.size() + " listeners.");
            for (SearchEventListener listener : listeners) {
                listener.onSearchCompleted(event);
            }
        }
    }
}