package infrastructure;

import org.junit.jupiter.api.Test;
import org.search.infrastructure.ConfigurationLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurationLoaderTest {

    @Test
    void testLoadPropertiesSuccessfully() throws IOException {
        // Setup a mock class loader that returns a valid properties file
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        InputStream mockInputStream = new ByteArrayInputStream("key=value\n".getBytes());

        // Mock the behavior of the class loader to return the simulated input stream
        when(mockClassLoader.getResourceAsStream("application.properties")).thenReturn(mockInputStream);

        // Create an instance of ConfigurationLoader with the mocked class loader
        ConfigurationLoader loader = new ConfigurationLoader(mockClassLoader);
        Properties properties = loader.loadProperties();

        assertNotNull(properties);
        assertEquals("value", properties.getProperty("key")); // Check the expected property value
    }

    @Test
    void testLoadPropertiesFileNotFound() {
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        when(mockClassLoader.getResourceAsStream("application.properties")).thenReturn(null);

        ConfigurationLoader loader = new ConfigurationLoader(mockClassLoader);
        Properties properties = loader.loadProperties();

        assertNotNull(properties);
        assertTrue(properties.isEmpty(), "Properties should be empty when the file is not found.");
    }

    @Test
    void testLoadPropertiesIOException() throws IOException {
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        InputStream mockInputStream = mock(InputStream.class);

        when(mockClassLoader.getResourceAsStream("application.properties")).thenReturn(mockInputStream);
        when(mockInputStream.read()).thenThrow(new IOException("Mock IOException"));

        ConfigurationLoader loader = new ConfigurationLoader(mockClassLoader);
        Properties properties = loader.loadProperties();

        assertNotNull(properties);
        assertTrue(properties.isEmpty(), "Properties should be empty on IOException.");
    }
}
