package org.search.infrasctructure;

import org.search.domain.exception.InvalidQueryException;
import org.search.domain.exception.ZipProcessingException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.MovieRepository;

import java.io.*;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.*;

public class ZipMovieRepository implements MovieRepository {
    private final String zipFilePath;

    public ZipMovieRepository(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }

    @Override
    public SearchResult searchInMovies(Query query) {
        if (query == null) {
            throw new InvalidQueryException("Query cannot be null or empty");
        }

        List<String> filesWithMatches = new ArrayList<>();
        int totalOccurrences = 0;

        String[] queryWords = query.getValue().toLowerCase().split("\\s+");

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

                    try {
                        // Instead of using BufferedReader, read directly from the ZipInputStream
                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            fileContent.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
                        }
                    } catch (IOException e) {
                        throw new ZipProcessingException("Error reading file: " + entry.getName(), e);
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
        } catch (FileNotFoundException e) {
            throw new org.search.domain.exception.FileNotFoundException("Zip file not found: " + zipFilePath, e);
        } catch (IOException e) {
            throw new ZipProcessingException("Error processing zip file: " + zipFilePath, e);
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

// Ensure we are only working with files inside the "data" folder and with a ".txt" extension
//                if (entry.getName().startsWith("data/") && entry.getName().endsWith(".txt")) {
//                    StringBuilder fileContent = new StringBuilder();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        fileContent.append(line).append(" ");
//                    }
//
//                    // Check if all query words are present in the file content
//                    String content = fileContent.toString().toLowerCase();
//                    boolean allWordsPresent = Arrays.stream(queryWords).allMatch(content::contains);
//                    if (allWordsPresent) {
//                        filesWithMatches.add(entry.getName());
//                        totalOccurrences++;
//                    }
//                }

//                error
//                if (entry.getName().startsWith("data/") && entry.getName().endsWith(".txt")) {
//                    StringBuilder fileContent = new StringBuilder();
//                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8))) {
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            fileContent.append(line).append(" ");
//                        }
//                    } catch (IOException e) {
//                        throw new ZipProcessingException("Error reading file: " + entry.getName(), e);
//                    }
//
//                    // Check if all query words are present in the file content
//                    String content = fileContent.toString().toLowerCase();
//                    boolean allWordsPresent = Arrays.stream(queryWords).allMatch(content::contains);
//                    if (allWordsPresent) {
//                        filesWithMatches.add(entry.getName());
//                        totalOccurrences++;
//                    }
//                }

//                error
//                if (entry.getName().startsWith("data/") && entry.getName().endsWith(".txt")) {
//                    StringBuilder fileContent = new StringBuilder();
//
//                    // Instead of using try-with-resources here, we manually close BufferedReader later
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
//                    String line;
//                    try {
//                        while ((line = reader.readLine()) != null) {
//                            fileContent.append(line).append(" ");
//                        }
//                    } catch (IOException e) {
//                        throw new ZipProcessingException("Error reading file: " + entry.getName(), e);
//                    } finally {
//                        // We need to ensure BufferedReader does not close ZipInputStream
//                        reader.close();
//                    }
//
//                    // Check if all query words are present in the file content
//                    String content = fileContent.toString().toLowerCase();
//                    boolean allWordsPresent = Arrays.stream(queryWords).allMatch(content::contains);
//                    if (allWordsPresent) {
//                        filesWithMatches.add(entry.getName());
//                        totalOccurrences++;
//                    }
//                }
