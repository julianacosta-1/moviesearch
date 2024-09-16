package org.search.domain;

import java.util.List;

public class SearchResult {
    private final int occurrenceCount;
    private final List<String> filesWithMatches;

    public SearchResult(int occurrenceCount, List<String> filesWithMatches) {
        this.occurrenceCount = occurrenceCount;
        this.filesWithMatches = filesWithMatches;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public List<String> getFilesWithMatches() {
        return filesWithMatches;
    }
}