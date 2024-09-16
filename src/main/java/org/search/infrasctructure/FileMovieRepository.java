package org.search.infrasctructure;

import org.search.domain.MovieRepository;
import org.search.domain.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileMovieRepository implements MovieRepository {

    private final String movieDirectory;

    public FileMovieRepository(String movieDirectory) {
        this.movieDirectory = movieDirectory;
    }

    @Override
    public SearchResult searchInMovies(String query) {
        List<String> filesWithMatches = new ArrayList<>();
        int totalOccurrences = 0;

        try {
            // Accessing the directory as a stream
            InputStream directoryStream = getClass().getClassLoader().getResourceAsStream(movieDirectory + "/");
            if (directoryStream == null) {
                throw new IOException("Directory not found: " + movieDirectory);
            }

            // Using BufferedReader to read directory contents
            BufferedReader reader = new BufferedReader(new InputStreamReader(directoryStream));
            List<String> fileNames = reader.lines().toList();

            // Process each file
            for (String fileName : fileNames) {
                InputStream fileStream = getClass().getClassLoader().getResourceAsStream(movieDirectory + "/" + fileName);
                if (fileStream == null) {
                    continue; // Skip files not found
                }

                try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream))) {
                    long occurrences = fileReader.lines()
                            .filter(line -> line.toLowerCase().contains(query.toLowerCase()))
                            .count();

                    if (occurrences > 0) {
                        filesWithMatches.add(fileName);
                        totalOccurrences += occurrences;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        filesWithMatches.sort(String::compareTo);  // Sort alphabetically
        return new SearchResult(totalOccurrences, filesWithMatches);
    }
}