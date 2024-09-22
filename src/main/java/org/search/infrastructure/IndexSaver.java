package org.search.infrastructure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class IndexSaver {
    public void saveIndex(List<IndexEntry> indexEntries, String indexFilePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexFilePath))) {
            oos.writeObject(indexEntries);
        }
    }
}


