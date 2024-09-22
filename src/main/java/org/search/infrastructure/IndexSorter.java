package org.search.infrastructure;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class IndexSorter {

    // Method to sort file names for each word in ascending order
    public static Map<String, Set<String>> sortIndexEntries(Map<String, Set<String>> invertedIndex) {
        return invertedIndex.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new TreeSet<>(entry.getValue())
                ));
    }
}

