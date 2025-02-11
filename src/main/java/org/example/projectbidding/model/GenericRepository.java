package org.example.projectbidding.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class GenericRepository<T extends Entity>{
    protected Connection connection;
    protected final String tableName;

    private static final Logger logger = LoggerFactory.getLogger(GenericRepository.class);

    protected GenericRepository(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Inserts a new entity into the database
     *
     * @param entity The entity to insert
     */
    public abstract void insert(T entity);

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return An Optional containing the entity if found.
     */
    public Optional<T> getById(Long id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }
        return Optional.empty();
    }

    /**
     * Retrieves all entities from the table.
     *
     * @return A list of all entities.
     */
    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }
        return entities;
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id The entity ID.
     */
    public void deleteById(Long id) {
        String query = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }
    }

    /**
     * Maps a ResultSet row to an entity.
     *
     * @param rs The ResultSet from the query.
     * @return The mapped entity.
     * @throws SQLException if an SQL error occurs.
     */
    public abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;


    public void update(Long id, Map<String, Object> updates) {

        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        updates.forEach((key, value) -> query.append(key).append(" = ?, "));
        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;
            for (Object value : updates.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setLong(index, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }
    }
}
