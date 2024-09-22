package org.search;

import org.search.application.SearchOrchestrator;
import org.search.application.SearchService;
import org.search.domain.exception.*;
import org.search.domain.repository.MovieRepository;
import org.search.domain.repository.InvertedIndexMovieRepository;
import org.search.infrastructure.IndexBuilder;
import org.search.infrastructure.LoggerConfig;
import org.search.utils.ParseArgument;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Set<String> cachedCompositeWords = new HashSet<>(Set.of("walt disney")); // Add "walt disney" to the cache

    public static void main(String[] args) throws java.io.FileNotFoundException {
        // Configure logger
        LoggerConfig.configureLogger();
        String searchTerm;

        // Parse command line arguments
        try {
            searchTerm = ParseArgument.parseArgument(args);
        } catch (IllegalArgumentException e) {
            logger.severe("Invalid arguments: " + e.getMessage());
            return;
        }

        String indexFilePath = "src/main/resources/index";
        String zipFilePath = "src/main/resources/movies.zip"; // Path to your zip file

        // Check if the index file exists or if searchTerm is a composite phrase
        File indexFile = new File(indexFilePath);
        boolean isCompositePhrase = searchTerm.contains(" ");

        if (!indexFile.exists() || (isCompositePhrase && !cachedCompositeWords.contains(searchTerm))) {
            if (isCompositePhrase) {
                // Remove the existing index file if the search term is a composite phrase
                if (indexFile.exists()) {
                    indexFile.delete();
                }
                cachedCompositeWords.add(searchTerm); // Cache the composite word
            }

            logger.info("Index file not found or search term is a new composite phrase. Building index...");
            try {
                IndexBuilder.buildIndex(zipFilePath, indexFilePath, searchTerm); // Pass composite phrase
                logger.info("Index built successfully.");
            } catch (FileNotFoundException e) {
                logger.severe("ZIP file not found: " + e.getMessage());
                return;
            } catch (ZipProcessingException e) {
                logger.severe("Error processing ZIP file: " + e.getMessage());
                return;
            } catch (IOException e) {
                logger.severe("Failed to build index: " + e.getMessage());
                return;
            }
        } else {
            logger.info("Using cached index for composite phrase: " + searchTerm);
        }

        // Initialize repository and services
        MovieRepository movieRepository;
        try {
            movieRepository = new InvertedIndexMovieRepository(indexFilePath);
        } catch (MovieSearchException e) {
            logger.severe("Failed to initialize repository: " + e.getMessage());
            return;
        }

        // Initialize service and orchestrator
        SearchService searchService = new SearchService(movieRepository);
        SearchOrchestrator orchestrator = new SearchOrchestrator(searchService);

        // Run the search orchestrator
        try {
            orchestrator.run(searchTerm);
        } catch (InvalidQueryException e) {
            logger.severe("Invalid search query: " + e.getMessage());
        } catch (SearchTermNotFoundException e) {
            logger.info("Search completed: " + e.getMessage());
        } catch (MovieSearchException e) {
            logger.severe("An error occurred during the search: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
        }
    }
}