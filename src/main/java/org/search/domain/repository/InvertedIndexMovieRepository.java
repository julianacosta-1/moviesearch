package org.search.domain.repository;


import org.search.domain.exception.FileNotFoundException;
import org.search.domain.exception.IndexFormatException;
import org.search.domain.exception.MovieSearchException;
import org.search.domain.exception.SearchTermNotFoundException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.infrastructure.IndexLoader;

import java.io.IOException;
import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

// in %.6f milliseconds

public class InvertedIndexMovieRepository implements MovieRepository {
    private final Map<String, Set<String>> invertedIndex;

    public InvertedIndexMovieRepository(String indexFilePath) {
        // Initialize the index only once and ensure it is cached
        try {
            this.invertedIndex = IndexLoader.loadIndex(indexFilePath);
        } catch (FileNotFoundException e) {
            throw new MovieSearchException("Index file not found: " + indexFilePath, e);
        } catch (IndexFormatException | IOException | ClassNotFoundException e) {
            throw new MovieSearchException("Index file format is invalid: " + indexFilePath, e);
        }
    }

    public SearchResult searchInMovies(Query query) {
        String searchTerm = query.getValue().toLowerCase();

        Set<String> searchResults = searchFilesWithTerm(invertedIndex, searchTerm);
        int occurrenceCount = searchResults.size();

        if (occurrenceCount == 0) {
            throw new SearchTermNotFoundException("No matches found for the search term: " + searchTerm);
        }

        SearchResult result = new SearchResult(occurrenceCount, new ArrayList<>(searchResults));
        return result;
    }

    private static Set<String> searchFilesWithTerm(Map<String, Set<String>> invertedIndex, String searchTerm) {
        double startTime = System.nanoTime();

        Set<String> result = invertedIndex.getOrDefault(searchTerm, Collections.emptySet()); // Return found files or an empty set
        double endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000;

        System.out.println(duration);
//        Logger.getLogger(InvertedIndexMovieRepository.class.getName())
//                .info(String.format("Search duration: %.10f milliseconds.\n", duration));

        return result;
    }
}