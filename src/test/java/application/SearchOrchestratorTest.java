package application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.search.domain.event.SearchEvent;
import org.search.domain.event.SearchEventListener;
import org.search.domain.exception.InvalidQueryException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.application.SearchOrchestrator;
import org.search.application.SearchService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchOrchestratorTest {

    @Mock
    private SearchService searchService;

    private SearchOrchestrator searchOrchestrator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchOrchestrator = new SearchOrchestrator(searchService);
    }

    @Test
    void testRunValidSearch() {
        // Given
        String searchTerm = "test";
        SearchResult searchResult = new SearchResult(1, Collections.singletonList("file1.txt"));
        when(searchService.search(new Query(searchTerm))).thenReturn(searchResult);

        // When
        searchOrchestrator.run(searchTerm);

        // Then
        ArgumentCaptor<SearchEventListener> listenerCaptor = ArgumentCaptor.forClass(SearchEventListener.class);
        verify(searchService).addSearchEventListener(listenerCaptor.capture());

        // Verify that the search service was called with the correct query
        verify(searchService).search(new Query(searchTerm));

        // Verify that the listener is called
        SearchEventListener listener = listenerCaptor.getValue();
        assertNotNull(listener);
        listener.onSearchCompleted(new SearchEvent(new Query(searchTerm), searchResult.getOccurrenceCount()));
    }

    @Test
    void testRunInvalidSearchQuery() {
        // Given
        String invalidSearchTerm = "";

        // When & Then
        InvalidQueryException thrown = assertThrows(InvalidQueryException.class, () -> {
            searchOrchestrator.run(invalidSearchTerm);
        });

        // Verify the exception message
        assertEquals("Query cannot be null or empty", thrown.getMessage());
        verify(searchService, never()).search(any(Query.class)); // Ensure search is never called
    }


    @Test
    void testRunNoOccurrencesFound() {
        // Given
        String searchTerm = "test";
        SearchResult searchResult = new SearchResult(0, Collections.emptyList());
        when(searchService.search(new Query(searchTerm))).thenReturn(searchResult);

        // When
        searchOrchestrator.run(searchTerm);

        // Then
        verify(searchService).addSearchEventListener(any());
        verify(searchService).search(new Query(searchTerm));
    }
}