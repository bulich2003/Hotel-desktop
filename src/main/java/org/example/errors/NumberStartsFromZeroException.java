package org.example.errors;

import org.apache.log4j.Logger;

public class NumberStartsFromZeroException extends Exception {
    private static final Logger logger = Logger.getLogger(NumberStartsFromZeroException.class.getName());
    public NumberStartsFromZeroException(String s) {
        super(s);
        logger.error(s);
    }
}
