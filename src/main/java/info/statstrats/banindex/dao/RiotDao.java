/**
 * Copyright (c) 2015 Desmond Silveira. All Rights Reserved.
 */
package info.statstrats.banindex.dao;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import info.statstrats.banindex.Match;
import info.statstrats.banindex.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * Returns true if the identified Match already exists in the data store.
     */
    public boolean hasMatch(Match match) throws SQLException {
        checkNotNull(match);

        try (PreparedStatement stmt = conn.prepareStatement(
                "select id from raw_match_data where region=? and match_id=?")) {
            stmt.setString(1, match.getRegion().toString());
            stmt.setLong(2, match.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Inserts the given raw match data into the data store.
     */
    public void insertMatch(Match match, String json) throws SQLException {
        checkNotNull(match);
        checkNotNull(json);

        try (PreparedStatement stmt = conn.prepareStatement(
                "insert into raw_match_data (region, match_id, json) values (?,?,?)")) {
            stmt.setString(1, match.getRegion().toString());
            stmt.setLong(2, match.getId());
            stmt.setString(3, json);
            stmt.execute();
        }
    }

    /**
     * Returns true if the identified NURF interval already has match ids in the data store.
     */
    public boolean hasNurfInterval(Region region, long beginDate) throws SQLException {
        checkNotNull(region);

        try (PreparedStatement stmt = conn.prepareStatement(
                "select id from nurf_interval where region=? and begin_date=?")) {
            stmt.setString(1, region.toString());
            stmt.setTimestamp(2, toTimestamp(beginDate));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
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
                "INSERT INTO nurf_interval (region, begin_date, match_id) VALUES (?,?,?)")) {
            for (long match : matches) {
                stmt.setString(1, region.toString().toLowerCase());
                stmt.setTimestamp(2, toTimestamp(beginDate));
                stmt.setLong(3, match);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Timestamp toTimestamp(long date) {
        return new Timestamp(date * 1000);
    }
}
