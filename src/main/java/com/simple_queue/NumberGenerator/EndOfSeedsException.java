package com.simple_queue.NumberGenerator;

/**
 * My custom exception class.
 */
class EndOfSeedsException extends ArrayIndexOutOfBoundsException {
    public EndOfSeedsException(String message) {
        super(message);
    }
}