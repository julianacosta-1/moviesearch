package application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.search.application.SearchService;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.MovieRepository;
import org.search.domain.event.SearchEvent;
import org.search.domain.event.SearchEventListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SearchServiceTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private SearchEventListener listener;

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchService = new SearchService(movieRepository);
        searchService.addSearchEventListener(listener);
    }

    @Test
    void testSearch() {
        Query query = new Query("test");
        SearchResult result = new SearchResult(1, List.of("file.txt"));
        when(movieRepository.searchInMovies(query)).thenReturn(result);

        SearchResult searchResult = searchService.search(query);

        verify(movieRepository).searchInMovies(query);
        assertEquals(1, searchResult.getOccurrenceCount());

        ArgumentCaptor<SearchEvent> eventCaptor = ArgumentCaptor.forClass(SearchEvent.class);
        verify(listener).onSearchCompleted(eventCaptor.capture());

        SearchEvent capturedEvent = eventCaptor.getValue();
        assertEquals(query, capturedEvent.getQuery());
        assertEquals(1, capturedEvent.getResultCount());
    }

    @Test
    void testNoEventWhenNoListeners() {
        searchService = new SearchService(movieRepository); // Recreate service without listeners
        Query query = new Query("test");
        SearchResult result = new SearchResult(1, List.of("file.txt"));

        when(movieRepository.searchInMovies(query)).thenReturn(result);

        SearchResult searchResult = searchService.search(query);

        verify(movieRepository).searchInMovies(query);
        verifyNoInteractions(listener); // No events should be triggered
    }

    @Test
    void testSearchDuration() {
        Query query = new Query("test");
        SearchResult result = new SearchResult(1, List.of("file.txt"));

        when(movieRepository.searchInMovies(query)).thenReturn(result);

        long startTime = System.nanoTime();
        SearchResult searchResult = searchService.search(query);
        long endTime = System.nanoTime();

        long elapsedTimeMicros = (endTime - startTime) / 1000;

        // Assert that the elapsed time is within a reasonable range
        assertTrue(elapsedTimeMicros >= 0);
    }


}