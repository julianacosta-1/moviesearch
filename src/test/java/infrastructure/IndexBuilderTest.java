package infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.search.domain.exception.ZipProcessingException;
import org.search.infrastructure.IndexBuilder;
import org.search.infrastructure.IndexEntry;
import org.search.infrastructure.IndexSaver;
import org.search.infrastructure.ZipFileProcessor;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class IndexBuilderTest {

    private ZipFileProcessor zipFileProcessor;
    private IndexSaver indexSaver;
    private IndexBuilder indexBuilder;

    @BeforeEach
    void setUp() {
        zipFileProcessor = Mockito.mock(ZipFileProcessor.class);
        indexSaver = Mockito.mock(IndexSaver.class);
        indexBuilder = new IndexBuilder(zipFileProcessor, indexSaver);
    }

    @Test
    void testBuildIndexSuccess() throws IOException {
        // Given
        String zipFilePath = "path/to/zip";
        String indexFilePath = "path/to/index";
        String compositePhrase = "walt disney";

        // Prepare the invertedIndex mock result
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        invertedIndex.put("disney", new HashSet<>(List.of("file1.txt")));
        invertedIndex.put("walt", new HashSet<>(List.of("file2.txt")));

        // Mock the zipFileProcessor behavior
        when(zipFileProcessor.processZipFile(zipFilePath, compositePhrase)).thenReturn(invertedIndex);

        // Capture the arguments passed to saveIndex()
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<IndexEntry>> captor = ArgumentCaptor.forClass(List.class);

        // When
        indexBuilder.buildIndex(zipFilePath, indexFilePath, compositePhrase);

        // Then
        verify(zipFileProcessor).processZipFile(zipFilePath, compositePhrase);
        verify(indexSaver).saveIndex(captor.capture(), eq(indexFilePath));

        // Check that the captured argument matches the expected list
        List<IndexEntry> expectedEntries = IndexEntry.createIndexEntries(invertedIndex);
        List<IndexEntry> actualEntries = captor.getValue();

        assertEquals(expectedEntries, actualEntries);
    }


    @Test
    void testBuildIndexThrowsZipProcessingException() throws ZipProcessingException, IOException {
        // Given
        String zipFilePath = "path/to/zip";
        String indexFilePath = "path/to/index";
        String compositePhrase = "walt disney";

        // Simulate an IOException thrown by the zipFileProcessor
        when(zipFileProcessor.processZipFile(zipFilePath, compositePhrase)).thenThrow(new IOException("ZIP processing error"));

        // When/Then
        ZipProcessingException thrown = assertThrows(ZipProcessingException.class, () -> {
            indexBuilder.buildIndex(zipFilePath, indexFilePath, compositePhrase);
        });

        // Ensure indexSaver.saveIndex is never called
        verify(indexSaver, never()).saveIndex(anyList(), eq(indexFilePath));

        // Assert that the cause of the ZipProcessingException is the original IOException
        assertEquals(IOException.class, thrown.getCause().getClass());
        assertEquals("ZIP processing error", thrown.getCause().getMessage());
    }

}
