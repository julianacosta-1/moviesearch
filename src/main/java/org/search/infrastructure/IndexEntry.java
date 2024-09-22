package org.search.infrastructure;

import java.io.Serializable;
import java.util.Set;

public class IndexEntry implements Serializable {
    private String word;
    private int frequency;
    private Set<String> fileNames;

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

    @Override
    public String toString() {
        return "IndexEntry{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                ", fileNames=" + fileNames +
                '}';
    }
}
