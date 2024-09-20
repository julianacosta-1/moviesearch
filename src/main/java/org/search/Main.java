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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

//public class Main{
//    public static void main(String[] args) {
//        String zipFilePath = "src/main/resources/movies.zip"; //args[0];
//        String indexFilePath = "src/main/resources/index"; //args[1];
//
//        try {
//            // Carregar o índice invertido do arquivo
//            Map<String, Set<String>> invertedIndex = loadIndex(indexFilePath);
//
//            // Termos de busca
//            String searchTerm = "walt"; // Por exemplo, "walt disney"
//            String[] searchTerms = searchTerm.toLowerCase().split("\\s+"); // Dividir por espaços
//
//            float startTime = System.nanoTime(); // Capturar o tempo de início da busca
//
//            // Realizar busca pelos termos utilizando o critério "AND"
//            Set<String> searchResults = searchFilesWithAllTerms(invertedIndex, searchTerms);
//
//            float endTime = System.nanoTime(); // Capturar o tempo de término
//            float duration = (endTime - startTime) / 1000000; // Converter para milissegundos
//
//            // Exibir os resultados da busca
//            if (searchResults != null && !searchResults.isEmpty()) {
//                System.out.println("Foram encontradas " + searchResults.size() + " ocorrências pelo termo \"" + searchTerm + "\".");
//                System.out.println("Os arquivos que possuem \"" + searchTerm + "\" são: ");
//                searchResults.forEach(System.out::println);
//            } else {
//                System.out.println("Nenhuma ocorrência foi encontrada pelo termo \"" + searchTerm + "\".");
//            }
//
//            // Exibir o tempo de busca
//            System.out.println("Tempo de busca: " + duration + " ms");
//
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Método para carregar o índice invertido do arquivo
//    @SuppressWarnings("unchecked")
//    private static Map<String, Set<String>> loadIndex(String indexFilePath) throws IOException, ClassNotFoundException {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexFilePath))) {
//            return (Map<String, Set<String>>) ois.readObject();
//        }
//    }
//
//    private static Set<String> searchFilesWithAllTerms(Map<String, Set<String>> invertedIndex, String[] searchTerms) {
//        Set<String> result = null;
//
//        for (String term : searchTerms) {
//            Set<String> filesContainingTerm = invertedIndex.get(term);
//
//            // If any term is not found, return empty set
//            if (filesContainingTerm == null) {
//                return Collections.emptySet(); // No matches
//            }
//
//            // Initialize the result with the files of the first term
//            if (result == null) {
//                result = new TreeSet<>(filesContainingTerm); // Use TreeSet to maintain sorted order
//            } else {
//                // Retain only files that contain all terms (intersection)
//                result.retainAll(filesContainingTerm);
//            }
//        }
//
//        // No need to sort again, TreeSet keeps elements sorted
//        return result != null ? result : Collections.emptySet();
//    }
//}


public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
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

        // Check if the index file exists
        File indexFile = new File(indexFilePath);
        if (!indexFile.exists()) {
            logger.info("Index file not found. Building index...");
            try {
                IndexBuilder.buildIndex(zipFilePath, indexFilePath);
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