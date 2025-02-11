package org.example.projectbidding.model;


import org.example.projectbidding.exception.EmptyFieldException;

/**
 * Represents an entity that requires validation before being used.
 * <p>
 * This sealed interface permits only {@link Bid} and {@link Project} to implement it.
 * Entities implementing this interface must provide a validation mechanism.
 */
public sealed interface Validatable permits Bid, Project{
    void validate() throws EmptyFieldException;
}
