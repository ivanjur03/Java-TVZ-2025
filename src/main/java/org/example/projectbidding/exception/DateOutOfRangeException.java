package org.example.projectbidding.exception;

/**
 * Exception thrown when a provided date is out of the acceptable range.
 * <p>
 * This exception is typically used to indicate that a date does not meet
 * the required constraints, such as a start date being after an end date.
 */
public class DateOutOfRangeException extends RuntimeException{
    public DateOutOfRangeException(String message) {
        super(message);
    }
}
