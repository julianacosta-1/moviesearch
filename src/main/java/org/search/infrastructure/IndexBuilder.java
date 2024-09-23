package org.search.infrastructure;

import org.search.domain.exception.IndexSaveException;
import org.search.domain.exception.ZipProcessingException;

import java.util.*;
import java.io.IOException;

public class IndexBuilder {
    private final ZipFileProcessor zipFileProcessor;
    private final IndexSaver indexSaver;

    public IndexBuilder(ZipFileProcessor zipFileProcessor, IndexSaver indexSaver) {
        this.zipFileProcessor = zipFileProcessor;
        this.indexSaver = indexSaver;
    }

    public void buildIndex(String zipFilePath, String indexFilePath, String compositePhrase) throws ZipProcessingException, IndexSaveException {
        try {
            Map<String, Set<String>> invertedIndex = zipFileProcessor.processZipFile(zipFilePath, compositePhrase);
            List<IndexEntry> indexEntries = IndexEntry.createIndexEntries(invertedIndex);
            indexSaver.saveIndex(indexEntries, indexFilePath);
        } catch (IOException e) {
            throw new ZipProcessingException("Failed to process ZIP file: " + zipFilePath, e);
        }
    }

}