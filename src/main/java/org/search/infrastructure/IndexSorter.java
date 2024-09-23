package org.search.infrastructure;

import org.search.domain.exception.IndexEmptyException;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class IndexSorter {

    // Method to sort file names for each word in ascending order
    public static Map<String, Set<String>> sortIndexEntries(Map<String, Set<String>> invertedIndex) {
        if (invertedIndex == null) {
            throw new IllegalArgumentException("Inverted index cannot be null");
        }

        if (invertedIndex.isEmpty()) {
            throw new IndexEmptyException("Inverted index is empty.");
        }

        return invertedIndex.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new TreeSet<>(entry.getValue())
                ));
    }
}

