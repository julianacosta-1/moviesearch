package infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.exception.ZipProcessingException;
import org.search.infrasctructure.InvertedIndexMovieRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvertedIndexMovieRepositoryTest {
    @Mock
    private ZipProcessingException zipProcessingException;

    private InvertedIndexMovieRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize with a valid path or mock the class that reads the file
        repository = new InvertedIndexMovieRepository("path/to/valid/zipfile");
    }

    @Test
    void testSearchInMovies() {
        // Assuming the repository was initialized with mock data
        Query query = new Query("test");
        SearchResult result = repository.searchInMovies(query);

        assertNotNull(result);
        assertEquals(1, result.getOccurrenceCount());
        assertTrue(result.getFilesWithMatches().contains("file.txt"));
    }

    @Test
    void testBuildInvertedIndexException() {
        // Simulate a scenario where an IOException is thrown
        repository = new InvertedIndexMovieRepository("path/to/invalid/zipfile");

        assertThrows(ZipProcessingException.class, () -> {
            repository.buildInvertedIndex("path/to/invalid/zipfile");
        });
    }
}
