package org.example.projectbidding.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Utility class for managing database connections.
 * <p>
 * This class provides methods to establish and close connections to the H2 database.
 * It uses {@link DriverManager} to obtain connections and logs errors if any occur.
 * <p>
 * This class is designed to be non-instantiable.
 */
public class Database {
    private static final String URL = "jdbc:h2:~/projekt";
    private static final String USER = "ivan";
    private static final String PASSWORD = "ivanica2003";


    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    /**
     * Establishes and returns a connection to the H2 database.
     * <p>
     * - Uses JDBC to connect to the configured database. <br>
     * - Logs an error if a connection cannot be established. <br>
     *
     * @return A {@link Connection} object if successful, otherwise {@code null}.
     */
    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(SQLException e){
            logger.error("SQL exception happened", e);
            return null;
        }
    }

    /**
     * Closes the provided database connection if it is not null.
     * <p>
     * - Ensures proper resource management by closing open connections. <br>
     * - Logs an error if an {@link SQLException} occurs while closing the connection. <br>
     *
     * @param con The {@link Connection} object to be closed.
     */

    public static void closeConnection(Connection con){
        if(con != null){
            try{
                con.close();
            }catch(SQLException e){
                logger.error("SQL exception happened", e);
            }

        }
    }



    private Database(){}
}
