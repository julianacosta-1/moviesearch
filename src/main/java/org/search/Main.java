package org.search;

import org.search.application.SearchOrchestrator;
import org.search.application.SearchService;
import org.search.domain.exception.IndexLoadException;
import org.search.domain.exception.MovieSearchException;
import org.search.domain.repository.InvertedIndexMovieRepository;
import org.search.infrastructure.*;
import org.search.utils.ParseArgument;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static String INDEX_FILE_PATH;
    private static String ZIP_FILE_PATH;

    public static void main(String[] args) {
        loadProperties();
        LoggerConfig.configureLogger();

        String searchTerm;
        try {
            searchTerm = ParseArgument.parseArgument(args);
        } catch (IllegalArgumentException e) {
            logAndExit(e.getMessage());
            return; // Exit early if there's an error
        }

        if (searchTerm == null) return;
        if (needsIndexRebuild(searchTerm)) buildIndex(searchTerm);
        executeSearch(searchTerm);
    }

    private static void loadProperties() {
        ConfigurationLoader loader = new ConfigurationLoader(Main.class.getClassLoader());
        Properties prop = loader.loadProperties();
        INDEX_FILE_PATH = prop.getProperty("index.file.path");
        ZIP_FILE_PATH = prop.getProperty("zip.file.path");
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
        } catch (IndexLoadException e) {
            logAndExit("Index loading error: " + e.getMessage());
        } catch (Exception e) {
            logAndExit("Unexpected error occurred: " + e.getMessage());
        }
    }

    private static void executeSearch(String searchTerm) {
        try {
            IndexLoader indexLoader = new IndexLoader();
            var movieRepository = new InvertedIndexMovieRepository(indexLoader, INDEX_FILE_PATH);
            new SearchOrchestrator(new SearchService(movieRepository)).run(searchTerm);
        } catch (MovieSearchException e) {
            logAndExit("Search failed: " + e.getMessage());
        } catch (Exception e) {
            logAndExit("An unexpected error occurred during search: " + e.getMessage());
        }
    }

    private static void logAndExit(String message) {
        logger.severe(message);
        System.exit(1);
    }
}