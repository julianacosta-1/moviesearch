package org.search.infrasctructure;

import org.search.domain.MovieRepository;
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
    public SearchResult searchInMovies(String query) {
        List<String> queryWords = Arrays.asList(query.toLowerCase().split("\\s+"));
        Set<String> resultFiles = new HashSet<>();

        // Find files containing all query words
        for (String word : queryWords) {
            resultFiles.retainAll(invertedIndex.getOrDefault(word, Collections.emptyList()));
        }

        return new SearchResult(resultFiles.size(), new ArrayList<>(resultFiles));
    }
}
