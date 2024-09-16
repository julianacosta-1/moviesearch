package org.search;

import org.search.application.SearchService;
import org.search.domain.MovieRepository;
import org.search.domain.SearchResult;
import org.search.infrasctructure.ZipMovieRepository;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
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

        // Perform the search
        SearchResult result = searchService.search(searchTerm);

        // Display the results
        System.out.printf("%d occurrences were found for the term \"%s\".%n", result.getOccurrenceCount(), searchTerm);
        System.out.println("The files that have \"" + searchTerm + "\" are:");
        result.getFilesWithMatches().forEach(System.out::println);
    }
}