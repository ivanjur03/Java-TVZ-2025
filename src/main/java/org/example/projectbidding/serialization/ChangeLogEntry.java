package org.example.projectbidding.serialization;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents a log entry that tracks changes made to an entity.
 * <p>
 * This class stores details about the field that was changed, its old and new values,
 * the role of the user who made the change, and a timestamp.
 * Implements {@link Serializable} for persistence.
 */
public class ChangeLogEntry implements Serializable {

    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private String changedByRole;
    private LocalDateTime timestamp;

    public ChangeLogEntry(String fieldChanged, String oldValue, String newValue, String changedByRole) {
        this.fieldChanged = fieldChanged;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedByRole = changedByRole;
        this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public String getFieldChanged() { return fieldChanged; }
    public String getOldValue() { return oldValue; }
    public String getNewValue() { return newValue; }
    public String getChangedByRole() { return changedByRole; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return timestamp + " | " + changedByRole + " changed " + fieldChanged +
                " from '" + oldValue + "' to '" + newValue + "'";
    }
}
