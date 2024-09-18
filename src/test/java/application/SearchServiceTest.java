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
import org.search.domain.exception.InvalidQueryException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testSearchInvalidQuery() {
        Query invalidQuery = new Query(null);

        assertThrows(InvalidQueryException.class, () -> searchService.search(invalidQuery));
    }
}
