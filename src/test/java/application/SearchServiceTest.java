package application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.search.application.SearchService;
import org.search.domain.event.SearchEvent;
import org.search.domain.event.SearchEventListener;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.MovieRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private MovieRepository movieRepository;
    private SearchService searchService;
    private SearchEventListener listener;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        searchService = new SearchService(movieRepository);
        listener = mock(SearchEventListener.class);
    }

    @Test
    void testSearchSuccessful() {
        // Given
        Query query = new Query("test");
        SearchResult expectedResult = new SearchResult(2, List.of("file1.txt", "file2.txt"));
        when(movieRepository.searchInMovies(query)).thenReturn(expectedResult);

        // Add listener
        searchService.addSearchEventListener(listener);

        // When
        SearchResult result = searchService.search(query);

        // Then
        assertEquals(expectedResult, result);
        verify(movieRepository).searchInMovies(query);
        ArgumentCaptor<SearchEvent> eventCaptor = ArgumentCaptor.forClass(SearchEvent.class);
        verify(listener).onSearchCompleted(eventCaptor.capture());
        assertEquals(query, eventCaptor.getValue().getQuery());
        assertEquals(2, eventCaptor.getValue().getResultCount());
    }

    @Test
    void testAddSearchEventListener() {
        // When
        searchService.addSearchEventListener(listener);
        // Then
        assertTrue(searchService.getListeners().contains(listener));
    }
}