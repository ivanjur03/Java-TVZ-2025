package org.example.projectbidding.exception;


/**
 * Exception thrown when user authentication fails due to invalid credentials.
 * <p>
 * This exception is typically used to indicate incorrect username or password input
 * during the login process.
 */
public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
