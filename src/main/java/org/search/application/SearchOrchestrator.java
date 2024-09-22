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
                logger.info(String.format("Foram encontradas %d ocorrências pelo termo \"%s\".",
                        event.getResultCount(), event.getQuery().getValue()));
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
        if (result.getOccurrenceCount() > 0) {
            result.getFilesWithMatches().forEach(logger::info);
        } else {
            logger.info("No occurrences found.");
        }
    }
}