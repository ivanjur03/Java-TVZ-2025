package org.example.projectbidding.exception;


/**
 * Exception thrown when an invalid bid amount is provided.
 * <p>
 * This exception is typically used to enforce constraints on bid values,
 * such as ensuring they are positive and within acceptable limits.
 */
public class InvalidBidAmountException extends RuntimeException{
    public InvalidBidAmountException(String message) {
        super(message);
    }
}
