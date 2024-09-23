package org.search.infrastructure;

import org.search.domain.exception.IndexEmptyException;
import org.search.domain.exception.IndexFormatException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Objects;

public class IndexEntry implements Serializable{
    private final String word;
    private final int frequency;
    private final Set<String> fileNames;

    public IndexEntry(String word, int frequency, Set<String> fileNames) {
        this.word = word;
        this.frequency = frequency;
        this.fileNames = fileNames;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public Set<String> getFileNames() {
        return fileNames;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntry that = (IndexEntry) o;
        return frequency == that.frequency &&
                Objects.equals(word, that.word) &&
                Objects.equals(fileNames, that.fileNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, frequency, fileNames);
    }

    @Override
    public String toString() {
        return "IndexEntry{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                ", fileNames=" + fileNames +
                '}';
    }

    public static List<IndexEntry> createIndexEntries(Map<String, Set<String>> invertedIndex) {
        if (invertedIndex == null) {
            throw new IndexFormatException("Inverted index cannot be null.");
        }
        if (invertedIndex.isEmpty()) {
            throw new IndexEmptyException("Inverted index cannot be empty.");
        }

        List<IndexEntry> entries = new ArrayList<>();
        invertedIndex.forEach((word, fileNames) -> {
            if (word == null || fileNames == null) {
                throw new IndexFormatException("Index entry cannot have null word or fileNames.");
            }
            int frequency = fileNames.size();
            entries.add(new IndexEntry(word, frequency, fileNames));
        });
        return entries;
    }

}
