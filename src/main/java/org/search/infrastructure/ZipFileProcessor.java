package org.search.infrastructure;

import org.search.domain.exception.ZipProcessingException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileProcessor {
    public Map<String, Set<String>> processZipFile(String zipFilePath, String compositePhrase) throws IOException {
        Map<String, Set<String>> invertedIndex = new HashMap<>();

        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            throw new FileNotFoundException("ZIP file not found: " + zipFilePath);
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    processTextFile(zipInputStream, invertedIndex, entry.getName(), compositePhrase);
                }
            }
        } catch (IOException e) {
            throw new ZipProcessingException("Error reading from ZIP file: " + zipFilePath, e);
        }

        return IndexSorter.sortIndexEntries(invertedIndex);
    }

    private void processTextFile(InputStream inputStream, Map<String, Set<String>> invertedIndex,
                                 String fileName, String compositePhrase) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String fileContent = reader.lines().collect(Collectors.joining(" ")).toLowerCase();

        if (fileContent.contains(compositePhrase.toLowerCase())) {
            invertedIndex.computeIfAbsent(compositePhrase, k -> new HashSet<>()).add(fileName);
        }

        for (String word : fileContent.split("\\s+")) {
            if (!word.equals(compositePhrase.toLowerCase())) {
                invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(fileName);
            }
        }
    }
}
