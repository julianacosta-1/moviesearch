package infrastructure;

import org.junit.jupiter.api.Test;
import org.search.infrastructure.IndexEntry;
import org.search.infrastructure.IndexLoader;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IndexLoaderTest {

    @Test
    void testLoadIndexSuccess() throws IOException, ClassNotFoundException {
        // Given
        List<IndexEntry> indexEntries = List.of(
                new IndexEntry("disney", 1, Set.of("file1.txt")),
                new IndexEntry("walt", 1, Set.of("file2.txt"))
        );

        // Create a temporary file to write the serialized indexEntries
        File tempFile = File.createTempFile("test-index", ".dat");
        tempFile.deleteOnExit();  // Ensure the file is deleted after the test

        // Serialize indexEntries to the temp file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {
            oos.writeObject(indexEntries);
        }

        // Create an instance of IndexLoader
        IndexLoader indexLoader = new IndexLoader();

        // Call the method to load the index from the temp file
        Map<String, Set<String>> result = indexLoader.loadIndex(tempFile.getAbsolutePath());

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("disney"));
        assertTrue(result.containsKey("walt"));
        assertEquals(Set.of("file1.txt"), result.get("disney"));
        assertEquals(Set.of("file2.txt"), result.get("walt"));
    }

    @Test
    void testLoadIndexIOException() throws IOException {
        // Given
        String indexFilePath = "path/to/corrupted-index";

        // Create a corrupted temporary file to simulate an IOException during loading
        File tempFile = File.createTempFile("corrupted-index", ".dat");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            // Write some random, corrupted data to the file
            fos.write("corrupted data".getBytes());
        }

        // Create an instance of IndexLoader
        IndexLoader indexLoader = new IndexLoader();

        // Expecting an IOException when trying to read the corrupted file
        IOException thrown = assertThrows(IOException.class, () -> indexLoader.loadIndex(tempFile.getAbsolutePath()));

        // Verify the exception message
        assertTrue(thrown.getMessage().contains("I/O error while loading index"));
    }
}
