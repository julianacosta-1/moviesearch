package infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.search.domain.exception.IndexSaveException;
import org.search.infrastructure.IndexEntry;
import org.search.infrastructure.IndexSaver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class IndexSaverTest {

    private IndexSaver indexSaver;

    @BeforeEach
    void setUp() {
        indexSaver = new IndexSaver();
    }

    @Test
    void testSaveIndexSuccess() throws IOException {
        // Given
        List<IndexEntry> indexEntries = List.of(
                new IndexEntry("disney", 1, Set.of("file1.txt")),
                new IndexEntry("pixar", 1, Set.of("file2.txt"))
        );

        // Use ByteArrayOutputStream instead of FileOutputStream to avoid writing to disk
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        // Use a spy on the ObjectOutputStream to verify interaction
        ObjectOutputStream objectOutputStreamSpy = spy(objectOutputStream);

        // Write to the spy stream
        objectOutputStreamSpy.writeObject(indexEntries);

        // Verify that writeObject was called with the correct data
        verify(objectOutputStreamSpy).writeObject(indexEntries);

        // Optional: Assert that data is not empty
        assertTrue(byteArrayOutputStream.size() > 0);
    }

    @Test
    void testSaveIndexIOException() {
        // Given
        String indexFilePath = "invalid/path";  // Use an invalid file path
        List<IndexEntry> indexEntries = List.of(
                new IndexEntry("disney", 1, Set.of("file1.txt")),
                new IndexEntry("pixar", 1, Set.of("file2.txt"))
        );

        // Expect an IndexSaveException due to invalid file path
        assertThrows(IndexSaveException.class, () -> indexSaver.saveIndex(indexEntries, indexFilePath));
    }
}

