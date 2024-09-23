package org.search.infrastructure;

import org.search.Main;
import org.search.domain.exception.ZipFileEmptyException;
import org.search.domain.exception.ZipProcessingException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileProcessor {

    public Map<String, Set<String>> processZipFile(String zipFilePath, String compositePhrase) throws IOException {
        Map<String, Set<String>> invertedIndex = new HashMap<>();

        try (InputStream zipInputStream = Main.class.getResourceAsStream(zipFilePath)) {
            if (zipInputStream == null) {
                throw new FileNotFoundException("ZIP file not found in resources: " + zipFilePath);
            }

            try (ZipInputStream zis = new ZipInputStream(zipInputStream)) {
                ZipEntry entry;
                boolean foundTextFile = false; // Track if we found any text files
                while ((entry = zis.getNextEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                        foundTextFile = true;
                        processTextFile(zis, invertedIndex, entry.getName(), compositePhrase);
                    }
                }
                if (!foundTextFile) {
                    throw new ZipFileEmptyException("No text files found in ZIP: " + zipFilePath);
                }
            }
        } catch (IOException e) {
            throw new ZipProcessingException("Error reading from ZIP file in resources: " + zipFilePath, e);
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