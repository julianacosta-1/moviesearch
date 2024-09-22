package infrastructure;

import org.search.domain.exception.ZipProcessingException;
import org.search.infrastructure.ZipFileProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ZipFileProcessorTest {

    private ZipFileProcessor zipFileProcessor;

    @BeforeEach
    void setUp() {
        zipFileProcessor = new ZipFileProcessor();
    }

    @Test
    void testProcessZipFileFileNotFound() {
        String zipFilePath = "/nonexistent.zip"; // Path to a non-existent ZIP file

        ZipProcessingException exception = assertThrows(ZipProcessingException.class, () -> {
            zipFileProcessor.processZipFile(zipFilePath, "phrase one");
        });

        // Check that the cause of the ZipProcessingException is a FileNotFoundException
        assertTrue(exception.getCause() instanceof FileNotFoundException);
        assertTrue(exception.getCause().getMessage().contains("ZIP file not found in resources: " + zipFilePath));
    }


    @Test
    void testProcessZipFileSuccessful() throws IOException {
        String zipFilePath = "/test.zip"; // Adjust based on where the file is located in resources

        // Call the method under test with the resource path
        Map<String, Set<String>> result = zipFileProcessor.processZipFile(zipFilePath, "phrase one");

        // Verify the results
        assertEquals(5, result.size()); // Updated to match the actual number of unique keys
        assertTrue(result.containsKey("phrase one")); // Check for composite phrase
        assertTrue(result.get("phrase one").contains("test/test.txt")); // Adjusted path
        assertTrue(result.containsKey("test"));
        assertTrue(result.get("test").contains("test/test.txt")); // Adjusted path
        assertTrue(result.containsKey("test2"));
        assertTrue(result.get("test2").contains("test/test.txt")); // Adjusted path
        assertTrue(result.containsKey("phrase")); // Ensure this key is checked
        assertTrue(result.get("phrase").contains("test/test.txt")); // Adjusted path
        assertTrue(result.containsKey("one")); // Add assertion for "one"
        assertTrue(result.get("one").contains("test/test.txt")); // Adjusted path
    }
}
