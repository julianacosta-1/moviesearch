package org.search.infrastructure;

import org.search.domain.exception.FileNotFoundException;
import org.search.domain.exception.IndexSaveException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class IndexSaver {
    public void saveIndex(List<IndexEntry> indexEntries, String indexFilePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexFilePath))) {
            oos.writeObject(indexEntries);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Index file not found: " + indexFilePath, e);
        } catch (IOException e) {
            throw new IndexSaveException("Failed to save index to: " + indexFilePath, e);
        }
    }
}