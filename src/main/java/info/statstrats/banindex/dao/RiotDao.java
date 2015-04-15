/**
 * Copyright (c) 2015 Local Corporation. All Rights Reserved.
 */
package info.statstrats.banindex.dao;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import info.statstrats.banindex.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author dsilveira
 */
public class RiotDao {

    private Connection conn;

    protected RiotDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds all matches to the database.
     *
     * @param region the {@code Region}
     * @param beginDate the Epoch time, in seconds
     * @param matches the match ids
     * @throws SQLException on database error
     */
    public void createAll(Region region, long beginDate, long[] matches)
            throws SQLException {
        checkNotNull(region);
        checkElementIndex(0, matches.length);

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO nurf_interval (region, begin_date, match) VALUES (?,?,?)")) {
            for (long match : matches) {
                stmt.setString(1, region.toString().toLowerCase());
                stmt.setTimestamp(2, new Timestamp(beginDate * 1000));
                stmt.setLong(3, match);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
