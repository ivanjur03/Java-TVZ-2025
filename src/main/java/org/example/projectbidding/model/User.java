package org.example.projectbidding.model;


/**
 * Abstract class representing a user in the project bidding system.
 * <p>
 * This class extends {@link Entity} to ensure that all users have a unique identifier.
 * It provides common attributes such as {@code username} and {@code password}.
 */
public abstract class User extends Entity{

    protected String username;
    protected String password;

    protected User(Long id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
