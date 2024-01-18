package org.example.errors;

import org.apache.log4j.Logger;

public class RepeatedValueException extends Exception {
    private static final Logger logger = Logger.getLogger(RepeatedValueException.class.getName());
    public RepeatedValueException(String s) {
        super(s);
        logger.error(s);
    }
}
