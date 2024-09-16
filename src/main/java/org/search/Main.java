package org.search;

import org.search.application.SearchService;
import org.search.domain.MovieRepository;
import org.search.domain.Query;
import org.search.domain.SearchResult;
import org.search.infrasctructure.ZipMovieRepository;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//public class Main {
//    private static final Logger logger = Logger.getLogger(Main.class.getName());
//
//    public static void main(String[] args) {
//        // Configure logger
//        Logger rootLogger = Logger.getLogger("");
//        rootLogger.setLevel(Level.INFO); // Set default log level
//        ConsoleHandler consoleHandler = new ConsoleHandler();
//        consoleHandler.setLevel(Level.INFO); // Set log level for console output
//        rootLogger.addHandler(consoleHandler);
//
//        if (args.length < 1) {
//            logger.warning("Please provide a search term.");
//            return;
//        }
//
//        String searchTerm = args[0];
//        String zipFilePath = "src/main/resources/movies.zip"; // Path to the ZIP file
//
//        // Initialize the repository
//        MovieRepository movieRepository = new ZipMovieRepository(zipFilePath);
//
//        // Initialize the service
//        SearchService searchService = new SearchService(movieRepository);
//
//        try {
//            Query query = new Query(searchTerm);
//            // Perform the search
//            SearchResult result = searchService.search(query);
//
//            // Display the results
//            logger.info(String.format("%d occurrences were found for the term \"%s\".", result.getOccurrenceCount(), searchTerm));
//            logger.info("The files that have \"" + searchTerm + "\" are:");
//            result.getFilesWithMatches().forEach(file -> logger.info(file));
//        } catch (Exception e) {
//            logger.severe("An error occurred: " + e.getMessage());
//        }
//    }
//}


public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a search term.");
            return;
        }

        String searchTerm = args[0];
        String zipFilePath = "src/main/resources/movies.zip"; // Path to the ZIP file

        // Initialize the repository
        MovieRepository movieRepository = new ZipMovieRepository(zipFilePath);

        // Initialize the service
        SearchService searchService = new SearchService(movieRepository);

        // Create a Query object
        Query query = new Query(searchTerm);

        // Perform the search
        SearchResult result = searchService.search(query);

        // Display the results
        System.out.printf("%d occurrences were found for the term \"%s\".%n", result.getOccurrenceCount(), searchTerm);
        if(result.getOccurrenceCount()>0){
            System.out.println("The files that have \"" + searchTerm + "\" are:");
        }
        result.getFilesWithMatches().forEach(System.out::println);
    }
}