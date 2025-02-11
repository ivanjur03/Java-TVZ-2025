package org.example.projectbidding.model;

/**
 * Represents an administrator user in the Project Bidding System.
 * <p>
 * This class extends {@link User} and inherits its properties
 */
public class Admin extends User{

    public Admin(Long id, String username, String password) {
        super(id, username, password);
    }


}

