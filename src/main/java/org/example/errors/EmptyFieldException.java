package org.example.errors;

import org.apache.log4j.Logger;

public class EmptyFieldException extends Exception {
    private static final Logger logger = Logger.getLogger(EmptyFieldException.class.getName());
    public EmptyFieldException(String s) {
        super(s);
        logger.error(s);
    }
}
