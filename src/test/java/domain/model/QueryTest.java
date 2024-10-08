package domain.model;

import org.junit.jupiter.api.Test;
import org.search.domain.exception.InvalidQueryException;
import org.search.domain.model.Query;

import static org.junit.jupiter.api.Assertions.*;

public class QueryTest {

    @Test
    void testValidQuery() {
        Query query = new Query("test");
        assertEquals("test", query.getValue());
    }

    @Test
    void testInvalidQueryNull() {
        InvalidQueryException thrownException = assertThrows(
                InvalidQueryException.class,
                () -> new Query(null)
        );
        assertEquals("Query cannot be null or empty.", thrownException.getMessage());
    }

    @Test
    void testInvalidQueryEmpty() {
        InvalidQueryException thrownException = assertThrows(
                InvalidQueryException.class,
                () -> new Query("")
        );
        assertEquals("Query cannot be null or empty.", thrownException.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        Query query1 = new Query("test");
        Query query2 = new Query("test");
        Query query3 = new Query("different");

        assertEquals(query1, query2);
        assertNotEquals(query1, query3);
        assertEquals(query1.hashCode(), query2.hashCode());
        assertNotEquals(query1.hashCode(), query3.hashCode());
    }

    @Test
    void testToString() {
        Query query = new Query("test");
        assertEquals("test", query.toString());
    }
}
