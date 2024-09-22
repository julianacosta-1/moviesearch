package infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.search.domain.exception.ZipProcessingException;
import org.search.domain.model.Query;
import org.search.domain.model.SearchResult;
import org.search.domain.repository.InvertedIndexMovieRepository;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

import java.io.*;

public class InvertedIndexMovieRepositoryIntegrationTest {
    private InvertedIndexMovieRepository repository;
    private File tempZipFile;

    @BeforeEach
    void setUp(){
        try {
            // Create a temporary zip file
            String zipContent = "test content";
            tempZipFile = File.createTempFile("test", ".zip");
            createZipFile(zipContent, tempZipFile);

            // Initialize repository with the path to the temporary zip file
            repository = new InvertedIndexMovieRepository(tempZipFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Setup failed: " + e.getMessage(), e);
        } catch (ZipProcessingException e) {
            throw new RuntimeException("Failed to create repository: " + e.getMessage(), e);
        }
    }

    @AfterEach
    void tearDown() {
        // Delete the temporary file after tests
        if (tempZipFile != null && tempZipFile.exists()) {
            tempZipFile.delete();
        }
    }

    @Test
    void testSearchInMovies() {
        Query query = new Query("test");
        SearchResult result = repository.searchInMovies(query);

        assertNotNull(result);
        assertEquals(1, result.getOccurrenceCount());
        assertTrue(result.getFilesWithMatches().contains("file.txt"));
    }

    @Test
    void testBuildInvertedIndexException() {
        // Test with an invalid file path, should throw ZipProcessingException
        assertThrows(ZipProcessingException.class, () -> {
            // Create a repository with an invalid path to trigger the exception
            InvertedIndexMovieRepository invalidRepository = new InvertedIndexMovieRepository("invalid/path/to/zipfile.zip");
            // Attempt to build the index, which should throw ZipProcessingException
            invalidRepository.buildInvertedIndex("invalid/path/to/zipfile.zip");
        });
    }

    private void createZipFile(String content, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            ZipEntry entry = new ZipEntry("file.txt");
            zos.putNextEntry(entry);
            zos.write(content.getBytes());
            zos.closeEntry();
            zos.finish();
        }
    }
}