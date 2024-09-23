package org.search.domain.repository;

import org.search.domain.exception.*;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.infrastructure.IndexLoader;

import java.io.IOException;
import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.logging.Logger;

public class InvertedIndexMovieRepository implements MovieRepository {
    private static final Logger logger = Logger.getLogger(InvertedIndexMovieRepository.class.getName());
    private final Map<String, Set<String>> invertedIndex;

    public InvertedIndexMovieRepository(IndexLoader indexLoader, String indexFilePath) {
        try {
            this.invertedIndex = indexLoader.loadIndex(indexFilePath);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Index file not found: " + indexFilePath, e);
        } catch (IndexFormatException | IOException e) {
            throw new IndexLoadException("Failed to load index from: " + indexFilePath, e);
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

        logger.info(String.format("Search duration: %.10f milliseconds.\n", duration));

        return result;
    }
}