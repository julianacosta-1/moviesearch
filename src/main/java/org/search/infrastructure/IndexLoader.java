package org.search.infrastructure;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexLoader {
    public static Map<String, Set<String>> loadIndex(String indexFilePath) throws IOException, ClassNotFoundException {
        List<IndexEntry> indexEntries;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexFilePath))) {
            indexEntries = (List<IndexEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new IOException("Index file not found: " + indexFilePath, e);
        } catch (IOException e) {
            throw new IOException("Failed to read index file: " + indexFilePath, e);
        }

        // Convert List<IndexEntry> to Map<String, Set<String>>
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        for (IndexEntry entry : indexEntries) {
            invertedIndex.put(entry.getWord(), entry.getFileNames());
        }

        return invertedIndex;
    }
}
