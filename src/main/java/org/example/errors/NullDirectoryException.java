package org.example.errors;

import org.apache.log4j.Logger;

public class NullDirectoryException extends Exception {
    private static final Logger logger = Logger.getLogger(NullDirectoryException.class.getName());
    public NullDirectoryException(String s) {
        super(s);
        logger.error(s);
    }
}



