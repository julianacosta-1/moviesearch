package org.search.infrastructure;

import org.search.domain.exception.ZipProcessingException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IndexBuilder {
    public static void buildIndex(String zipFilePath, String indexFilePath) throws FileNotFoundException {
        Map<String, Set<String>> invertedIndex = new HashMap<>();

        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            throw new FileNotFoundException("ZIP file not found: " + zipFilePath);
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
                    String fileContent = reader.lines().collect(Collectors.joining(" "));
                    String[] words = fileContent.toLowerCase().split("\\s+");

                    for (String word : words) {
                        invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            throw new ZipProcessingException("Error reading from ZIP file: " + zipFilePath, e);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexFilePath))) {
            oos.writeObject(invertedIndex);
        } catch (IOException e) {
            throw new ZipProcessingException("Error saving index to file: " + indexFilePath, e);
        }
    }
}