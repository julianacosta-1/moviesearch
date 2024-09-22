package infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.search.infrastructure.ZipFileProcessor;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ZipFileProcessorTest {

    private ZipFileProcessor zipFileProcessor;

    @BeforeEach
    void setUp() {
        zipFileProcessor = new ZipFileProcessor();
    }

    @Test
    void testProcessZipFileFileNotFound() {
        String zipFilePath = "nonexistent.zip";
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            zipFileProcessor.processZipFile(zipFilePath, "test");
        });
        assertTrue(exception.getMessage().contains("ZIP file not found: " + zipFilePath));
    }

    @Test
    void testProcessZipFileSuccessful() throws IOException {
        // Create a temporary ZIP file
        File tempZipFile = File.createTempFile("test", ".zip");
        tempZipFile.deleteOnExit(); // Ensure it gets deleted after the test

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(tempZipFile))) {
            ZipEntry entry = new ZipEntry("test.txt");
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write("phrase test test2".getBytes());
            zipOutputStream.closeEntry();
        }

        // Call the method under test with the file path
        Map<String, Set<String>> result = zipFileProcessor.processZipFile(tempZipFile.getAbsolutePath(), "phrase");

        // Verify the results
        assertEquals(3, result.size()); // Adjust based on the expected number of unique keys
        assertTrue(result.containsKey("phrase"));
        assertTrue(result.get("phrase").contains("test.txt"));
        assertTrue(result.containsKey("test"));
        assertTrue(result.get("test").contains("test.txt"));
        assertTrue(result.containsKey("test2"));
        assertTrue(result.get("test2").contains("test.txt"));
    }
}