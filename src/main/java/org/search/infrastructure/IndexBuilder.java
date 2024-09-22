package org.search.infrastructure;

import java.util.*;
import java.io.IOException;

public class IndexBuilder {
    private final ZipFileProcessor zipFileProcessor;
    private final IndexSaver indexSaver;

    public IndexBuilder(ZipFileProcessor zipFileProcessor, IndexSaver indexSaver) {
        this.zipFileProcessor = zipFileProcessor;
        this.indexSaver = indexSaver;
    }

    public void buildIndex(String zipFilePath, String indexFilePath, String compositePhrase) throws IOException {
        Map<String, Set<String>> invertedIndex = zipFileProcessor.processZipFile(zipFilePath, compositePhrase);
        List<IndexEntry> indexEntries = IndexEntry.createIndexEntries(invertedIndex);
        indexSaver.saveIndex(indexEntries, indexFilePath);
    }
}