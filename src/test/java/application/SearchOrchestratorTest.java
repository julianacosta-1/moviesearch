package application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.search.application.SearchOrchestrator;
import org.search.application.SearchService;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.exception.InvalidQueryException;
import org.search.domain.exception.MovieSearchException;
import org.search.domain.event.SearchEvent;
import org.search.domain.event.SearchEventListener;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SearchOrchestratorTest {
    @Mock
    private SearchService searchService;
    @Mock
    private SearchEventListener listener;

    private SearchOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orchestrator = new SearchOrchestrator(searchService);
        //orchestrator.addSearchEventListener(listener);
    }

    @Test
    void testRunSuccess() {
        Query query = new Query("test");
        SearchResult result = new SearchResult(1, List.of("file.txt"));
        when(searchService.search(query)).thenReturn(result);

        orchestrator.run("test");

        ArgumentCaptor<SearchEvent> eventCaptor = ArgumentCaptor.forClass(SearchEvent.class);
        verify(searchService).addSearchEventListener(any());
        verify(listener).onSearchCompleted(eventCaptor.capture());

        SearchEvent capturedEvent = eventCaptor.getValue();
        assertEquals("test", capturedEvent.getQuery().getValue());
        assertEquals(1, capturedEvent.getResultCount());
    }

    @Test
    void testRunInvalidQuery() {
        doThrow(new InvalidQueryException("Invalid query")).when(searchService).search(any());

        orchestrator.run("invalid");

        verify(searchService).search(any());
    }
}

