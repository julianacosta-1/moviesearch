package infrastructure;

import org.junit.jupiter.api.Test;
import org.search.infrastructure.IndexEntry;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IndexEntryTest {

    @Test
    void testCreateIndexEntries() {
        // Given: a map of words to file names
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        invertedIndex.put("disney", new HashSet<>(Set.of("file1.txt", "file2.txt")));
        invertedIndex.put("pixar", new HashSet<>(Set.of("file3.txt")));

        // When: calling createIndexEntries
        List<IndexEntry> entries = IndexEntry.createIndexEntries(invertedIndex);

        // Then: the list should contain IndexEntry objects with correct frequency
        assertNotNull(entries);
        assertEquals(2, entries.size());

        // Verify that the entries contain correct data
        IndexEntry disneyEntry = new IndexEntry("disney", 2, Set.of("file1.txt", "file2.txt"));
        IndexEntry pixarEntry = new IndexEntry("pixar", 1, Set.of("file3.txt"));

        assertTrue(entries.contains(disneyEntry));
        assertTrue(entries.contains(pixarEntry));
    }
}