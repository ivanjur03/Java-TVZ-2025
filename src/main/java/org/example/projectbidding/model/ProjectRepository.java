package org.example.projectbidding.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Repository class for managing database operations related to projects.
 * <p>
 * This class extends {@link GenericRepository} to provide common CRUD operations
 * while implementing project-specific functionalities.
 */
public class ProjectRepository extends GenericRepository<Project>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectRepository.class);
    public ProjectRepository(Connection connection) {
        super(connection, "Project");
    }
    /**
     * Inserts a new project into the database.
     * <p>
     * - Uses a prepared statement to insert project details, including name, description, start date, and end date. <br>
     * - Logs an error if an {@link SQLException} occurs.
     *
     * @param project The {@link Project} entity to insert into the database.
     */
    @Override
    public void insert(Project project) {
        String query = "INSERT INTO Project (project_name, project_description, project_start_date, project_end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getProjectDescription());
            stmt.setDate(3, Date.valueOf(project.getProjectStartDate()));
            stmt.setDate(4, Date.valueOf(project.getProjectEndDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL exception happened",e);
        }
    }

    /**
     * Maps a {@link ResultSet} row to a {@link Project} entity.
     * <p>
     * - Retrieves project attributes from the result set. <br>
     * - Constructs and returns a {@code Project} object using the builder pattern.
     *
     * @param rs The result set containing project data.
     * @return A new {@code Project} entity populated with data from the result set.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public Project mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Project.ProjectBuilder()
                .setId(rs.getLong("id"))
                .setProjectName(rs.getString("project_name"))
                .setProjectDescription(rs.getString("project_description"))
                .setProjectStartDate(rs.getDate("project_start_date").toLocalDate())
                .setProjectEndDate(rs.getDate("project_end_date").toLocalDate())
                .build();

    }
}
