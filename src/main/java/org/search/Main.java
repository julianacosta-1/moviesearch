package org.search;

import org.search.application.SearchOrchestrator;
import org.search.application.SearchService;
import org.search.domain.exception.MovieSearchException;
import org.search.domain.repository.InvertedIndexMovieRepository;
import org.search.infrastructure.*;
import org.search.utils.ParseArgument;

import java.io.File;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String INDEX_FILE_PATH = "src/main/resources/index";
    private static final String ZIP_FILE_PATH = "src/main/resources/movies.zip";

    public static void main(String[] args) {
        LoggerConfig.configureLogger();

        String searchTerm = parseArguments(args);
        if (searchTerm == null) return;

        if (needsIndexRebuild(searchTerm)) buildIndex(searchTerm);

        executeSearch(searchTerm);
    }

    private static String parseArguments(String[] args) {
        try {
            return ParseArgument.parseArgument(args);
        } catch (IllegalArgumentException e) {
            logAndExit(e.getMessage());
            return null; // Will not reach here, but helps with compilation
        }
    }

    private static boolean needsIndexRebuild(String searchTerm) {
        File indexFile = new File(INDEX_FILE_PATH);
        return !indexFile.exists() || searchTerm.contains(" ");
    }

    private static void buildIndex(String searchTerm) {
        logger.info("Building index...");
        try {
            if (searchTerm.contains(" ")) new File(INDEX_FILE_PATH).delete();
            new IndexBuilder(new ZipFileProcessor(), new IndexSaver())
                    .buildIndex(ZIP_FILE_PATH, INDEX_FILE_PATH, searchTerm);
            logger.info("Index built successfully.");
        } catch (Exception e) {
            logAndExit("Failed to build index: " + e.getMessage());
        }
    }

    private static void executeSearch(String searchTerm) {
        try {
            IndexLoader indexLoader = new IndexLoader();
            var movieRepository = new InvertedIndexMovieRepository(indexLoader, INDEX_FILE_PATH);
            new SearchOrchestrator(new SearchService(movieRepository)).run(searchTerm);
        } catch (MovieSearchException e) {
            logAndExit("Failed to initialize repository: " + e.getMessage());
        }
    }

    private static void logAndExit(String message) {
        logger.severe(message);
        System.exit(1);
    }
}