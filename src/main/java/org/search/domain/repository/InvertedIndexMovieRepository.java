package org.search.domain.repository;

import org.search.domain.exception.FileNotFoundException;
import org.search.domain.exception.IndexFormatException;
import org.search.domain.exception.MovieSearchException;
import org.search.domain.exception.SearchTermNotFoundException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.infrastructure.IndexLoader;

import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.TreeSet;
import java.util.logging.Logger;

// in %.6f milliseconds

public class InvertedIndexMovieRepository implements MovieRepository {
    private final Map<String, Set<String>> invertedIndex;

    public InvertedIndexMovieRepository(String indexFilePath) {
        // Initialize the index only once and ensure it is cached
        try {
            this.invertedIndex = IndexLoader.loadIndex(indexFilePath);
        } catch (FileNotFoundException e) {
            throw new MovieSearchException("Index file not found: " + indexFilePath, e);
        } catch (IndexFormatException e) {
            throw new MovieSearchException("Index file format is invalid: " + indexFilePath, e);
        }
    }

    @Override
    public SearchResult searchInMovies(Query query) {

        String[] searchTerms = query.getValue().toLowerCase().split("\\s+");

        float startTime = System.nanoTime(); // Start timing

        Set<String> searchResults = searchFilesWithAllTerms(invertedIndex, searchTerms);
        int occurrenceCount = searchResults.size();

        if (occurrenceCount == 0) {
            throw new SearchTermNotFoundException("No matches found for the search terms: " + query.getValue());
        }

        float endTime = System.nanoTime(); // End timing
        float duration = (endTime - startTime) / 1_000_000;

        Logger.getLogger(InvertedIndexMovieRepository.class.getName())
                .info(String.format("Search duration: %.10f milliseconds.\n", duration)); // Convert to milliseconds

        return new SearchResult(occurrenceCount, new ArrayList<>(searchResults));
    }

    private static Set<String> searchFilesWithAllTerms(Map<String, Set<String>> invertedIndex, String[] searchTerms) {
        Set<String> result = null;

        for (String term : searchTerms) {
            Set<String> filesContainingTerm = invertedIndex.get(term);

            if (filesContainingTerm == null) {
                return Collections.emptySet(); // No matches
            }

            if (result == null) {
                result = new TreeSet<>(filesContainingTerm); // Initialize with the first term
            } else {
                result.retainAll(filesContainingTerm); // Perform intersection
            }
        }

        return result != null ? result : Collections.emptySet();
    }
}