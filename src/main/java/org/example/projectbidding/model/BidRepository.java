package org.example.projectbidding.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Repository class for managing database operations related to bids.
 * <p>
 * This class extends {@link GenericRepository} to provide common CRUD operations
 * while implementing specific bid-related functionalities.
 */
public class BidRepository extends GenericRepository<Bid>{

    private static final Logger logger = LoggerFactory.getLogger(BidRepository.class);

    public BidRepository(Connection connection) {
        super(connection, "Bid");
    }

    /**
     * Inserts a new bid into the database.
     * <p>
     * - Uses a prepared statement to insert bid details, including project ID, contractor ID,
     * bid amount, and bid date. <br>
     * - Logs an error if an {@link SQLException} occurs.
     *
     * @param bid The {@link Bid} entity to insert into the database.
     */
    @Override
    public void insert(Bid bid) {
        String query = "INSERT INTO Bid (project_id, contractor_id, bid_amount, bid_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, bid.getProject().getId());
            stmt.setLong(2, bid.getContractor().getId());
            stmt.setBigDecimal(3, bid.getBidAmount());
            stmt.setDate(4, Date.valueOf(bid.getBidDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL exception happened", e);

        }
    }

    /**
     * Maps a {@link ResultSet} row to a {@link Bid} entity.
     * <p>
     * - Retrieves project and contractor IDs from the result set. <br>
     * - Fetches related {@link Project} and {@link Contractor} entities from their respective repositories. <br>
     * - Constructs and returns a {@code Bid} object using the builder pattern.
     *
     * @param rs The result set containing bid data.
     * @return A new {@code Bid} entity populated with data from the result set.
     * @throws SQLException If the associated project or contractor is not found.
     */
    @Override
    public Bid mapResultSetToEntity(ResultSet rs) throws SQLException {
        Long projectId = rs.getLong("project_id");
        Long contractorId = rs.getLong("contractor_id");

        ProjectRepository projectRepository = new ProjectRepository(connection);
        Project project = projectRepository.getById(projectId).orElseThrow(() -> new SQLException("Project not found"));

        ContractorRepository contractorRepository = new ContractorRepository(connection);
        Contractor contractor = contractorRepository.getById(contractorId).orElseThrow(() -> new SQLException("Contractor not found"));

        return new Bid.BidBuilder()
                .setId(rs.getLong("id"))
                .setProject(project)
                .setContractor(contractor)
                .setBidAmount(rs.getBigDecimal("bid_amount"))
                .setBidDate(rs.getDate("bid_date").toLocalDate())
                .build();



    }
}
