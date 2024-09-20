package org.search.infrastructure;

import org.search.domain.exception.IndexFormatException;
import org.search.domain.exception.IndexLoadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Set;

public class IndexLoader {
    @SuppressWarnings("unchecked")
    public static Map<String, Set<String>> loadIndex(String indexFilePath) throws IndexLoadException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexFilePath))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                return (Map<String, Set<String>>) obj;
            } else {
                throw new IndexFormatException("Loaded object is not a Map.");
            }
        } catch (IOException e) {
            throw new IndexLoadException("Failed to load index from file: " + indexFilePath, e);
        } catch (ClassNotFoundException e) {
            throw new IndexLoadException("Class not found while loading index.", e);
        }
    }
}