package org.example.projectbidding.model;

import org.example.projectbidding.exception.InvalidCredentialsException;
import org.example.projectbidding.main.HelloApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages user authentication, password hashing, and credential storage.
 * <p>
 * This class provides methods to authenticate users, add new users, update credentials,
 * and remove users from the system. User credentials are stored in text files for simplicity.
 */
public class LoginManager{

    private static final String ADMIN_FILE = "dat/admin.txt";
    private static final String USER_FILE = "dat/contractors.txt";

    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    private LoginManager(){}

    /**
     * Hashes a password using the SHA-256 algorithm.
     *
     * @param password The plain text password to hash.
     * @return The hashed password as a hexadecimal string, or {@code null} if hashing fails.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while hashing password", e);
        }
        return null;
    }

    /**
     * Authenticates a user by checking the provided username and password against stored credentials.
     * <p>
     * - Admin users are authenticated using a separate admin credentials file. <br>
     * - The password is hashed before being compared. <br>
     * - If authentication is successful, the logged-in user is stored in {@link HelloApplication}.
     *
     * @param username The username to authenticate.
     * @param password The plain text password to check.
     * @return {@code true} if authentication is successful.
     * @throws InvalidCredentialsException If authentication fails.
     */
    public static Boolean authenticate(String username, String password) throws InvalidCredentialsException {
        String fileName;
        if(username.equals("admin")){
            fileName=ADMIN_FILE;
        }else{
            fileName=USER_FILE;
        }
        String hashedPassword = hashPassword(password);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    HelloApplication.setLoggedInUser(parts[0]);
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error("Error while reading credentials file", e);
        }
        throw new InvalidCredentialsException("Invalid credentials");
    }

    /**
     * Adds a new user to the system by storing their username and hashed password.
     * <p>
     * - The username and password are provided as a {@link javafx.util.Pair}, where
     *   the key represents the username and the value represents the plain-text password. <br>
     * - The password is hashed before being stored. <br>
     * - The credentials are appended to the user credentials file. <br>
     * - Logs an error if an {@link IOException} occurs while writing to the file.
     *
     * @param namePass A {@link Pair} containing the username as the key
     *                 and the plain-text password as the value.
     */
    public static void addUser(Pair<String , String> namePass) {

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(namePass.getKey() + ":" + hashPassword(namePass.getValue()) + "\n");
        } catch (IOException e) {
            logger.error("Error while reading credentials file", e);
        }
    }

    /**
     * Removes a user from the user file
     * @param username The username to be removed
     * @return true if the user was found and removed, false otherwise
     */
    public static void removeUser(String username) {
        modifyUserFile(username, null, null, true);
    }

    /**
     * Updates a user's username and/or password
     * @param username The current username
     * @param newUsername The new username (can be null if only updating password)
     * @param newPassword The new password (can be null if only updating username)
     * @return true if the user was found and updated, false otherwise
     */
    public static void updateUser(String username, String newUsername, String newPassword) {
        modifyUserFile(username, newUsername, newPassword, false);
    }

    /**
     * Internal method to modify or remove a user's credentials.
     * <p>
     * - Reads all user credentials from the file. <br>
     * - If {@code isRemove} is {@code true}, the user is removed. <br>
     * - Otherwise, the username and/or password are updated. <br>
     * - Writes the modified list of users back to the file.
     *
     * @param username The username to modify or remove.
     * @param newUsername The new username (if updating).
     * @param newPassword The new password (if updating).
     * @param isRemove {@code true} if the user should be removed, {@code false} to update.
     */
    private static void modifyUserFile(String username, String newUsername, String newPassword, boolean isRemove) {
        List<String> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username)) {

                    if (!isRemove) {
                        String updatedUsername = (!newUsername.isEmpty()) ? newUsername : username;
                        String updatedPassword = (!newPassword.isEmpty()) ? hashPassword(newPassword) : parts[1];
                        users.add(updatedUsername + ":" + updatedPassword);
                    }

                } else {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            logger.error("Error while reading credentials file", e);
        }



        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String user : users) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Error while reading credentials file", e);

        }

    }


}
