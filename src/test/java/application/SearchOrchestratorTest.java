//package application;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.search.application.SearchOrchestrator;
//import org.search.application.SearchService;
//import org.search.domain.model.Query;
//import org.search.domain.model.SearchResult;
//import org.search.infrastructure.LoggerConfig;
//import java.util.Collections;
//import java.util.logging.Logger;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class SearchOrchestratorTest {
//
//    @Mock
//    private SearchService searchService;
//
//    @InjectMocks
//    private SearchOrchestrator searchOrchestrator;
//
//    private final Logger logger = Logger.getLogger(SearchOrchestratorTest.class.getName());
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        LoggerConfig.configureLogger();
//    }
//
//    @Test
//    public void testInteractionWithSearchService() {
//        String searchTerm = "testTerm";
//        Query query = new Query(searchTerm);
//        SearchResult searchResult = new SearchResult(1, Collections.singletonList("file.txt"));
//        when(searchService.search(any(Query.class))).thenReturn(searchResult);
//
//        searchOrchestrator.run(searchTerm);
//
//        verify(searchService).addSearchEventListener(any());
//        verify(searchService).search(query);
//    }
//}
