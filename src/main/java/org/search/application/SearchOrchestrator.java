package org.search.application;

import org.search.domain.exception.InvalidQueryException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import java.util.logging.Logger;

public class SearchOrchestrator {
    private final SearchService searchService;
    private final Logger logger = Logger.getLogger(SearchOrchestrator.class.getName());

    public SearchOrchestrator(SearchService searchService) {
        this.searchService = searchService;
    }

    public void run(String searchTerm) {
        try {
            searchService.addSearchEventListener(event -> {
                logger.info(String.format("Search completed for query '%s': %d occurrences found in %d microseconds.",
                        event.getQuery().getValue(), event.getResultCount(), event.getElapsedTime()));
            });

            Query query = new Query(searchTerm);
            SearchResult result = searchService.search(query);
            printResults(result, searchTerm);

        } catch (InvalidQueryException e) {
            logger.severe("Invalid search query: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
        }
    }

    private void printResults(SearchResult result, String searchTerm) {
        logger.info(String.format("%d occurrences were found for the term \"%s\".", result.getOccurrenceCount(), searchTerm));
        if (result.getOccurrenceCount() > 0) {
            logger.info("The files that have \"" + searchTerm + "\" are:");
            result.getFilesWithMatches().forEach(file -> logger.info(file));
        } else {
            logger.info("No occurrences found.");
        }
    }
}

