package utils;

import org.search.utils.ParseArgument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParseArgumentTest {

    @Test
    void testParseArgumentValidInput() {
        String[] args = {"searchTerm"};
        String result = ParseArgument.parseArgument(args);
        assertEquals("searchTerm", result, "The search term should be correctly parsed.");
    }

    @Test
    void testParseArgumentNoInput() {
        String[] args = {};
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ParseArgument.parseArgument(args),
                "Expected parseArgument() to throw, but it didn't"
        );
        assertEquals("Search term not provided.", thrown.getMessage(), "The error message should be correct.");
    }

    @Test
    void testParseArgumentInsufficientInput() {
        String[] args = {"term1", "term2"};
        String result = ParseArgument.parseArgument(args);
        assertEquals("term1", result, "The method should return the first argument.");
    }
}
