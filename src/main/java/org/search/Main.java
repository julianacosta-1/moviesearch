package org.search;

import org.search.application.SearchOrchestrator;
import org.search.application.SearchService;
import org.search.domain.exception.FileNotFoundException;
import org.search.domain.exception.ZipProcessingException;
import org.search.infrasctructure.InvertedIndexMovieRepository;
import org.search.infrasctructure.LoggerConfig;
import org.search.domain.repository.MovieRepository;

import org.search.utils.ParseArgument;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LoggerConfig.configureLogger();
        String searchTerm;

        try {
            searchTerm = ParseArgument.parseArgument(args);
        } catch (IllegalArgumentException e) {
            logger.severe("Invalid arguments: " + e.getMessage());
            return;
        }

        String zipFilePath = "src/main/resources/movies.zip";

        try {
            MovieRepository movieRepository = new InvertedIndexMovieRepository(zipFilePath);
            SearchService searchService = new SearchService(movieRepository);

            SearchOrchestrator orchestrator = new SearchOrchestrator(searchService);
            orchestrator.run(searchTerm);

        } catch (FileNotFoundException e) {
            logger.severe("File not found: " + e.getMessage());
        } catch (ZipProcessingException e) {
            logger.severe("Error processing ZIP file: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
        }
    }
}