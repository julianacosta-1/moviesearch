package org.search.infrastructure;

import org.search.domain.exception.ZipProcessingException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IndexBuilder {

    // Method to build the inverted index from a ZIP file
    public static void buildIndex(String zipFilePath, String indexFilePath, String compositePhrase) throws IOException {
        Map<String, Set<String>> invertedIndex = new HashMap<>();

        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            throw new FileNotFoundException("ZIP file not found: " + zipFilePath);
        }

        // Process the ZIP file and build the inverted index
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
                    String fileContent = reader.lines().collect(Collectors.joining(" ")).toLowerCase();

                    // If the file content contains the composite phrase, add it to the inverted index
                    if (fileContent.contains(compositePhrase.toLowerCase())) {
                        invertedIndex.computeIfAbsent(compositePhrase, k -> new HashSet<>()).add(entry.getName());
                    }

                    // For each word in the file content, add it to the inverted index
                    for (String word : fileContent.split("\\s+")) {
                        if (!word.equals(compositePhrase.toLowerCase())) {
                            invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(entry.getName());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ZipProcessingException("Error reading from ZIP file: " + zipFilePath, e);
        }

        // Sort file names for each word in ascending order
        invertedIndex = IndexSorter.sortIndexEntries(invertedIndex);

        // Create a list of index entries (IndexEntry) with words and their frequencies
        List<IndexEntry> indexEntries = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : invertedIndex.entrySet()) {
            int frequency = entry.getValue().size(); // Frequency is the size of the set of files
            Set<String> fileNames = entry.getValue(); // Set of file names
            indexEntries.add(new IndexEntry(entry.getKey(), frequency, fileNames)); // Add new entry
        }

        // Order entries by frequency (descending), with the composite phrase first
        indexEntries.sort((e1, e2) -> {
            if (e1.getWord().equalsIgnoreCase(compositePhrase)) return -1;
            if (e2.getWord().equalsIgnoreCase(compositePhrase)) return 1;
            return Integer.compare(e2.getFrequency(), e1.getFrequency());
        });

        // Save the index to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexFilePath))) {
            oos.writeObject(indexEntries);
        } catch (IOException e) {
            throw new ZipProcessingException("Error saving index to file: " + indexFilePath, e);
        }
    }
}