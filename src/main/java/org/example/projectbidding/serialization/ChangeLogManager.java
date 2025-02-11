package org.example.projectbidding.serialization;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the logging and retrieval of changes made in the system.
 * <p>
 * This class handles serialization and deserialization of {@link ChangeLogEntry} objects
 * to and from a binary file for persistence.
 * <p>
 * Uses synchronization to ensure thread safety when writing and reading changes.
 */
public class ChangeLogManager {
    private static final String FILE_NAME = "dat/change_log.bin";
    private static final Object LOCK = new Object();

    private static final Logger logger = LoggerFactory.getLogger(ChangeLogManager.class);

    private ChangeLogManager() {}

    /**
     * Logs a new change entry by appending it to the change log.
     * <p>
     * - Reads the current change log from the file. <br>
     * - Appends the new change entry. <br>
     * - Writes the updated list back to the file. <br>
     * - Uses synchronization to ensure thread safety.
     *
     * @param entry The {@link ChangeLogEntry} to be logged.
     */
    public static void logChange(ChangeLogEntry entry) {
        synchronized (LOCK) {
            List<ChangeLogEntry> changes = readChanges();
            changes.add(entry);
            writeChangesToFile(changes);
        }
    }

    /**
     * Reads and returns the list of logged changes from the change log file.
     * <p>
     * - If the file does not exist, returns an empty list. <br>
     * - Deserializes the list of {@link ChangeLogEntry} objects from the binary file. <br>
     * - Uses synchronization to ensure thread safety.
     *
     * @return A list of {@code ChangeLogEntry} objects representing logged changes.
     */
    public static List<ChangeLogEntry> readChanges() {
        synchronized (LOCK) {
            File file = new File(FILE_NAME);
            if (!file.exists()) return new ArrayList<>();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                return (List<ChangeLogEntry>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                logger.error("An error occurred while reading change log", e);
                return new ArrayList<>();
            }
        }
    }

    /**
     * Writes the list of change log entries to the file for persistence.
     * <p>
     * - Serializes the list of {@link ChangeLogEntry} objects. <br>
     * - Uses synchronization to ensure thread safety. <br>
     * - Logs an error if an {@link IOException} occurs.
     *
     * @param changes The list of changes to be written to the file.
     */
    private static void writeChangesToFile(List<ChangeLogEntry> changes) {
        synchronized (LOCK) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(changes);
            } catch (IOException e) {
                logger.error("An error occurred while writing change log", e);
            }
        }
    }
}

