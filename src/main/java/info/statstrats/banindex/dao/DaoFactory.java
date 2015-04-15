/**
 * Copyright (c) 2015 Desmond Silveira. All rights reserved.
 */
package info.statstrats.banindex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author dsilveira
 */
public class DaoFactory {

    private static final Logger logger = LogManager.getLogger();

    private String connStr;
    private Connection conn;

    /**
     * Constructor.
     *
     * @param connStr the JDBC connection string
     */
    public DaoFactory(String connStr) {
        this.connStr = connStr;
    }

    /**
     * Instantiates a new {@link Connection} that all DAOs will use.
     *
     * @throws SQLException if the {@code Connection} could not be created
     */
    public void beginConnectionScope() throws SQLException {
        logger.trace("Establishing connection: {}", connStr);
        conn = DriverManager.getConnection(connStr);
    }

    /**
     * Closes the common {@link Connection}.
     *
     * @throws SQLException if the {@code Connection} could not be closed
     */
    public void endConnectionScope() throws SQLException {
        if (conn == null) {
            return;
        }
        conn.close();
    }

//    /**
//     * @param table the table
//     * @return the {@link BiddedSearchDao}
//     */
//    public BiddedSearchDao createBiddedSearchDao(String table) {
//        return new BiddedSearchDaoImpl(conn, table);
//    }

    public RiotDao createRiotDao() {
        return new RiotDao(conn);
    }

}
