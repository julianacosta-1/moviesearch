package org.search.infrasctructure;

import org.search.domain.MovieRepository;
import org.search.domain.Query;
import org.search.domain.SearchResult;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InvertedIndexMovieRepository implements MovieRepository {
    private final Map<String, List<String>> invertedIndex = new HashMap<>();

    public InvertedIndexMovieRepository(String zipFilePath) {
        // Index files at startup
        buildInvertedIndex(zipFilePath);
    }

    private void buildInvertedIndex(String zipFilePath) {
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
