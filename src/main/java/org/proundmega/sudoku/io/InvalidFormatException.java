package org.proundmega.sudoku.io;

public class InvalidFormatException extends RuntimeException {

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
