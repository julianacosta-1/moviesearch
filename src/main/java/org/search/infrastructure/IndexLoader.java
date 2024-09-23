package org.search.infrastructure;

import org.search.domain.exception.FileNotFoundException;
import org.search.domain.exception.IndexFormatException;
import org.search.domain.exception.IndexLoadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class IndexLoader {
    public Map<String, Set<String>> loadIndex(String indexFilePath) throws IOException {
        List<IndexEntry> indexEntries = readIndexEntries(indexFilePath);

        // Convert List<IndexEntry> to Map<String, Set<String>>
        if (indexEntries == null || indexEntries.isEmpty()) {
            throw new IndexFormatException("Index entries are invalid or empty.");
        }

        Map<String, Set<String>> invertedIndex = new HashMap<>();
        for (IndexEntry entry : indexEntries) {
            invertedIndex.put(entry.getWord(), entry.getFileNames());
        }

        return invertedIndex;
    }

    private List<IndexEntry> readIndexEntries(String indexFilePath) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexFilePath))) {
            return (List<IndexEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Index file not found: " + indexFilePath, e);
        } catch (ClassNotFoundException e) {
            throw new IndexLoadException("Class not found while loading index: " + indexFilePath, e);
        } catch (IOException e) {
            throw new IndexLoadException("I/O error while loading index: " + indexFilePath, e);
        }
    }
}

