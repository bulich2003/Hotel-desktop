package org.example.errors;

import org.apache.log4j.Logger;

public class TwoPointsInFileNameException extends Exception {
    private static final Logger logger = Logger.getLogger(TwoPointsInFileNameException.class.getName());
    public TwoPointsInFileNameException(String s) {
        super(s);
        logger.error(s);
    }
}
