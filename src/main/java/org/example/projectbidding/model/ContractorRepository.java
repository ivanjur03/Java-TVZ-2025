package org.example.projectbidding.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Repository class for managing database operations related to contractors.
 * <p>
 * This class extends {@link GenericRepository} to provide common CRUD operations
 * while implementing contractor-specific functionalities.
 */
public class ContractorRepository extends GenericRepository<Contractor>{

    private static final Logger logger = LoggerFactory.getLogger(ContractorRepository.class);


    public ContractorRepository(Connection connection) {
        super(connection, "Contractor");

    }


    /**
     * Inserts a new contractor into the database.
     * <p>
     * - Uses a prepared statement to insert contractor details, including username, password, email, and rating. <br>
     * - Logs an error if an {@link SQLException} occurs.
     *
     * @param contractor The {@link Contractor} entity to insert into the database.
     */
    public void insert(Contractor contractor) {
        String query = "INSERT INTO Contractor (username, password, email, rating) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, contractor.getUsername());
            stmt.setString(2, contractor.getPassword());
            stmt.setString(3, contractor.getEmail());
            stmt.setBigDecimal(4, contractor.getRating());
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }
    }
    /**
     * Maps a {@link ResultSet} row to a {@link Contractor} entity.
     * <p>
     * - Retrieves contractor attributes from the result set. <br>
     * - Fetches the contractor's bid history. <br>
     * - Constructs and returns a {@code Contractor} object using the builder pattern.
     *
     * @param rs The result set containing contractor data.
     * @return A new {@code Contractor} entity populated with data from the result set.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public Contractor mapResultSetToEntity(ResultSet rs) throws SQLException {


        return new Contractor.ContractorBuilder()
                .setId(rs.getLong("id"))
                .setUsername(rs.getString("username"))
                .setPassword(rs.getString("password"))
                .setEmail(rs.getString("email"))
                .setRating(rs.getBigDecimal("rating"))
                .setBidList(getBidsForContractor(rs.getLong("id")))
                .build();
    }

    /**
     * Retrieves a comma-separated list of project names that the contractor has bid on.
     * <p>
     * - Executes a SQL query to find projects associated with the contractor's bids. <br>
     * - Returns "No Bids" if the contractor has not placed any bids. <br>
     *
     * @param contractorId The ID of the contractor.
     * @return A comma-separated string of project names or "No Bids" if none exist.
     */
    public String getProjectNamesForContractor(Long contractorId) {

        String query = "SELECT p.project_name FROM Project p " +
                "JOIN Bid b ON p.id = b.project_id " +
                "WHERE b.contractor_id = ?";

        Set<String> projectNames = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, contractorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projectNames.add(rs.getString("project_name"));
                }
            }
        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }

        return projectNames.isEmpty() ? "No Bids" : String.join(", ", projectNames);
    }

    /**
     * Retrieves a list of bids placed by a specific contractor.
     * <p>
     * - Executes a SQL query to fetch bids associated with the given contractor ID. <br>
     * - Maps the results into a list of {@link Bid} objects. <br>
     * - Logs an error if an {@link SQLException} occurs.
     *
     * @param contractorId The ID of the contractor.
     * @return A list of bids placed by the contractor.
     */
    public List<Bid> getBidsForContractor(Long contractorId) {
        String query = "SELECT * FROM Bid WHERE contractor_id = ?";
        List<Bid> bids = new ArrayList<>();
        ProjectRepository projectRepository = new ProjectRepository(Database.getConnection());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, contractorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bids.add(new Bid.BidBuilder()
                            .setId(rs.getLong("id"))
                            .setProject(projectRepository.getById(rs.getLong("project_id")).get())
                            .setBidAmount(rs.getBigDecimal("bid_amount"))
                            .setBidDate(rs.getDate("bid_date").toLocalDate())
                            .build());
                }
            }
        } catch (SQLException e) {
            logger.error("SQL exception happened", e);
        }
        return bids;
    }






}
