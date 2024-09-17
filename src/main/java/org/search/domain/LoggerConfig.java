package org.search.domain;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerConfig {

    public static void configureLogger() {
        // Configure logger with custom formatter
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO); // Set default log level

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO); // Set log level for console output

        // Set a custom formatter to remove timestamp and class name
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%s: %s%n", record.getLevel(), record.getMessage());
            }
        });

        // Replace the default handler
        rootLogger.getHandlers()[0].setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%s: %s%n", record.getLevel(), record.getMessage());
            }
        });
    }
}
