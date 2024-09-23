package infrastructure;

import org.junit.jupiter.api.Test;
import org.search.domain.exception.IndexEmptyException;
import org.search.infrastructure.IndexSorter;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class IndexSorterTest {

    @Test
    void testSortIndexEntries() {
        // Given: an unsorted map of words to file names
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        invertedIndex.put("disney", new HashSet<>(Set.of("file3.txt", "file1.txt", "file2.txt")));
        invertedIndex.put("walt", new HashSet<>(Set.of("file5.txt", "file4.txt")));

        // When: we call sortIndexEntries
        Map<String, Set<String>> sortedIndex = IndexSorter.sortIndexEntries(invertedIndex);

        // Then: each set of file names should be sorted in ascending order
        assertNotNull(sortedIndex);
        assertEquals(2, sortedIndex.size());

        // Verify disney entries are sorted
        assertEquals(new TreeSet<>(Set.of("file1.txt", "file2.txt", "file3.txt")), sortedIndex.get("disney"));

        // Verify walt entries are sorted
        assertEquals(new TreeSet<>(Set.of("file4.txt", "file5.txt")), sortedIndex.get("walt"));
    }

    @Test
    void testSortIndexEntriesEmptyMap() {
        // Given: an empty map
        Map<String, Set<String>> invertedIndex = new HashMap<>();

        // Then: expect IndexEmptyException when we call sortIndexEntries
        assertThrows(IndexEmptyException.class, () -> IndexSorter.sortIndexEntries(invertedIndex));
    }

    @Test
    void testSortIndexEntriesSingleEntry() {
        // Given: a map with a single entry
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        invertedIndex.put("pixar", new HashSet<>(Set.of("file2.txt", "file1.txt")));

        // When: we call sortIndexEntries
        Map<String, Set<String>> sortedIndex = IndexSorter.sortIndexEntries(invertedIndex);

        // Then: the single entry should be sorted
        assertNotNull(sortedIndex);
        assertEquals(1, sortedIndex.size());
        assertEquals(new TreeSet<>(Set.of("file1.txt", "file2.txt")), sortedIndex.get("pixar"));
    }
}
