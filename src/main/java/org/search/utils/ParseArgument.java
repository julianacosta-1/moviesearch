package org.search.utils;

import java.util.logging.Logger;

public class ParseArgument {
    public static String parseArgument(String[] args) {
        if (args.length < 1) {
            Logger.getLogger(ParseArgument.class.getName()).warning("Please provide a search term.");
            throw new IllegalArgumentException("Search term not provided.");
        }
        return args[0];
    }
}