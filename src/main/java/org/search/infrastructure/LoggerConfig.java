package org.search.infrastructure;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerConfig {

    public static void configureLogger() {
        // Configure logger with custom formatter
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO); // Set default log level

        // Check if there's already a handler attached to the root logger
        if (rootLogger.getHandlers().length > 0) {
            // Modify the existing handler's formatter
            rootLogger.getHandlers()[0].setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    // Remove timestamp, class name, and customize format
                    return String.format("%s%n",record.getMessage());
                }
            });
        }
    }
}