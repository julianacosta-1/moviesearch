package org.search.infrasctructure;

import org.search.domain.SearchResult;
import org.search.domain.MovieRepository;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.*;

public class ZipMovieRepository implements MovieRepository {
    private final String zipFilePath;

    public ZipMovieRepository(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }

    @Override
    public SearchResult searchInMovies(String query) {
        List<String> filesWithMatches = new ArrayList<>();
        int totalOccurrences = 0;

        String[] queryWords = query.toLowerCase().split("\\s+");

        // Measure start time
        long startTime = System.nanoTime();

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // Skip directory entries
                if (entry.isDirectory()) {
                    continue;
                }

                // Ensure we are only working with files inside the "data" folder and with a ".txt" extension
                if (entry.getName().startsWith("data/") && entry.getName().endsWith(".txt")) {
                    StringBuilder fileContent = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append(" ");
                    }

                    // Check if all query words are present in the file content
                    String content = fileContent.toString().toLowerCase();
                    boolean allWordsPresent = Arrays.stream(queryWords).allMatch(content::contains);
                    if (allWordsPresent) {
                        filesWithMatches.add(entry.getName());
                        totalOccurrences++;
                    }
                }

                zipInputStream.closeEntry();  // Close the current entry
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Measure end time
        long endTime = System.nanoTime();

        // Calculate elapsed time in microseconds
        long elapsedTimeMicros = (endTime - startTime) / 1000;

        // Check if the search process took more than 0.01 milliseconds (10 microseconds)
        if (elapsedTimeMicros > 10) {
            System.out.println("Search took too long: " + elapsedTimeMicros + " microseconds");
        } else {
            System.out.println("Search completed within 0.01 milliseconds: " + elapsedTimeMicros + " microseconds");
        }

        filesWithMatches.sort(String::compareTo);  // Sort alphabetically
        return new SearchResult(totalOccurrences, filesWithMatches);
    }


}