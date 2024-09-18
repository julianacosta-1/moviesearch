package org.search.infrasctructure;

import org.search.domain.exception.FileNotFoundException;
import org.search.domain.exception.ZipProcessingException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.MovieRepository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InvertedIndexMovieRepository implements MovieRepository {
    private final Map<String, List<String>> invertedIndex = new HashMap<>();

    public InvertedIndexMovieRepository(String zipFilePath) {
        // Index files at startup
        buildInvertedIndex(zipFilePath);
    }

    public void buildInvertedIndex(String zipFilePath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
                    String fileContent = reader.lines().collect(Collectors.joining(" "));
                    String[] words = fileContent.toLowerCase().split("\\s+");

                    for (String word : words) {
                        invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SearchResult searchInMovies(Query query) {
        List<String> queryWords = Arrays.asList(query.toString().toLowerCase().split("\\s+"));
        Set<String> resultFiles = null;

        // Iterar sobre as palavras da consulta
        for (String word : queryWords) {
            List<String> filesContainingWord = invertedIndex.getOrDefault(word, Collections.emptyList());

            if (resultFiles == null) {
                // Inicializa resultFiles com os arquivos que contêm a primeira palavra
                resultFiles = new HashSet<>(filesContainingWord);
            } else {
                // Faz a interseção para garantir que todos os arquivos contenham todas as palavras
                resultFiles.retainAll(filesContainingWord);
            }

            // Se resultFiles ficar vazio, podemos parar a busca
            if (resultFiles.isEmpty()) {
                break;
            }
        }

        // Se resultFiles ainda for nulo, inicializa como um conjunto vazio
        if (resultFiles == null) {
            resultFiles = new HashSet<>();
        }

        return new SearchResult(resultFiles.size(), new ArrayList<>(resultFiles));
    }

//    @Override
//    public SearchResult searchInMovies(Query query) {
//        List<String> queryWords = Arrays.asList(query.toString().toLowerCase().split("\\s+"));
//        Set<String> resultFiles = new HashSet<>();
//
//        // Find files containing all query words
//        for (String word : queryWords) {
//            resultFiles.retainAll(invertedIndex.getOrDefault(word, Collections.emptyList()));
//        }
//
//        return new SearchResult(resultFiles.size(), new ArrayList<>(resultFiles));
//    }
}

//public class InvertedIndexMovieRepository implements MovieRepository {
//    private static final Logger logger = Logger.getLogger(InvertedIndexMovieRepository.class.getName());
//    private final Map<String, List<String>> invertedIndex = new HashMap<>();
//
//    public InvertedIndexMovieRepository(String zipFilePath) {
//        try {
//            buildInvertedIndex(zipFilePath);
//        } catch (FileNotFoundException e) {
//            logger.severe("File not found: " + e.getMessage());
//            throw e;
//        } catch (ZipProcessingException e) {
//            logger.severe("Error processing ZIP file: " + e.getMessage());
//            throw e;
//        } catch (Exception e) {
//            logger.severe("Unexpected error: " + e.getMessage());
//            throw new RuntimeException("Unexpected error occurred", e);
//        }
//    }
//
//    private void buildInvertedIndex(String zipFilePath) throws FileNotFoundException, ZipProcessingException {
//        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
//            ZipEntry entry;
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
//                    processZipEntry(zipInputStream, entry.getName());
//                }
//            }
//        } catch (IOException e) {
//            logger.severe("IO error while processing ZIP file: " + e.getMessage());
//            throw new ZipProcessingException("Error processing ZIP file", e);
//        }
//    }
//
//    private void processZipEntry(ZipInputStream zipInputStream, String fileName) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream))) {
//            String fileContent = reader.lines().collect(Collectors.joining(" "));
//            indexFileContent(fileName, fileContent);
//        }
//    }
//
//    private void indexFileContent(String fileName, String content) {
//        String[] words = content.toLowerCase().split("\\s+");
//        for (String word : words) {
//            invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(fileName);
//        }
//    }
//
//    @Override
//    public SearchResult searchInMovies(Query query) {
//        List<String> queryWords = Arrays.asList(query.toString().toLowerCase().split("\\s+"));
//        Set<String> resultFiles = null;
//
//        for (String word : queryWords) {
//            List<String> filesContainingWord = invertedIndex.getOrDefault(word, Collections.emptyList());
//
//            if (resultFiles == null) {
//                resultFiles = new HashSet<>(filesContainingWord);
//            } else {
//                resultFiles.retainAll(filesContainingWord);
//            }
//
//            if (resultFiles.isEmpty()) {
//                break;
//            }
//        }
//
//        if (resultFiles == null) {
//            resultFiles = new HashSet<>();
//        }
//
//        return new SearchResult(resultFiles.size(), new ArrayList<>(resultFiles));
//    }
//}