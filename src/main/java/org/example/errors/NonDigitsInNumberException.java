package org.example.errors;

import org.apache.log4j.Logger;

public class NonDigitsInNumberException extends Exception {
    private static final Logger logger = Logger.getLogger(NonDigitsInNumberException.class.getName());
    public NonDigitsInNumberException(String s) {
        super(s);
        logger.error(s);
    }
}
