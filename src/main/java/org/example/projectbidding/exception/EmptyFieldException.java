package org.example.projectbidding.exception;


/**
 * Exception thrown when a required input field is left empty.
 * <p>
 * This exception is typically used to enforce mandatory input validation
 * in forms and data entry processes.
 */
public class EmptyFieldException extends Exception{
    public EmptyFieldException(String message) {
        super(message);
    }
}
