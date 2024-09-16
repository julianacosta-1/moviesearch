//package application;
//
//import org.junit.jupiter.api.Test;
//import org.search.application.SearchService;
//import org.search.domain.MovieRepository;
//import org.search.domain.SearchResult;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class SearchServiceTest {
//
//    @Test
//    public void testSearchReturnsCorrectResults() {
//        MovieRepository mockRepo = mock(MovieRepository.class);
//        SearchResult mockResult = new SearchResult(2, List.of("file1.txt", "file2.txt"));
//        when(mockRepo.searchInMovies("test query")).thenReturn(mockResult);
//
//        SearchService service = new SearchService(mockRepo);
//        SearchResult result = service.search("test query");
//
//        assertEquals(2, result.getOccurrenceCount());
//        assertEquals(2, result.getFilesWithMatches().size());
//    }
//}