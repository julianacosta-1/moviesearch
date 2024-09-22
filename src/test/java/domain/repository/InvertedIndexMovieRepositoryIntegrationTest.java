package domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.search.domain.exception.MovieSearchException;
import org.search.domain.exception.SearchTermNotFoundException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.InvertedIndexMovieRepository;
import org.search.infrastructure.IndexLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InvertedIndexMovieRepositoryTest {

    private InvertedIndexMovieRepository repository;
    private IndexLoader indexLoaderMock;
    private Map<String, Set<String>> mockIndex;

    @BeforeEach
    void setUp() throws IOException, ClassNotFoundException {
        // Prepare the mock index data
        mockIndex = new HashMap<>();
        mockIndex.put("test", Set.of("file1.txt", "file2.txt"));

        // Mock the IndexLoader
        indexLoaderMock = mock(IndexLoader.class);
        when(indexLoaderMock.loadIndex(anyString())).thenReturn(mockIndex);

        // Initialize the repository with the mocked IndexLoader
        repository = new InvertedIndexMovieRepository(indexLoaderMock, "dummy/path");
    }


    @Test
    void testConstructorFileNotFound() throws IOException, ClassNotFoundException {
        // Mock the behavior to throw an exception for an invalid path
        when(indexLoaderMock.loadIndex("invalid/path")).thenThrow(new MovieSearchException("Index file not found"));

        assertThrows(MovieSearchException.class, () -> {
            new InvertedIndexMovieRepository(indexLoaderMock, "invalid/path");
        });
    }

    @Test
    void testSearchInMoviesFound() {
        Query query = new Query("test");
        SearchResult result = repository.searchInMovies(query);

        assertEquals(2, result.getOccurrenceCount());
        assertTrue(result.getFilesWithMatches().contains("file1.txt"));
        assertTrue(result.getFilesWithMatches().contains("file2.txt"));
    }

    @Test
    void testSearchInMoviesNotFound() {
        Query query = new Query("nonexistent");
        assertThrows(SearchTermNotFoundException.class, () -> {
            repository.searchInMovies(query);
        });
    }

    @Test
    void testSearchInMoviesEmptyResult() {
        mockIndex.put("anotherTerm", Set.of("file3.txt"));
        Query query = new Query("notInIndex");

        assertThrows(SearchTermNotFoundException.class, () -> {
            repository.searchInMovies(query);
        });
    }
}