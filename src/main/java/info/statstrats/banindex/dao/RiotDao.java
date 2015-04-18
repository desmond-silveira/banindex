/**
 * Copyright (c) 2015 Desmond Silveira. All Rights Reserved.
 */
package info.statstrats.banindex.dao;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import info.statstrats.banindex.Champion;
import info.statstrats.banindex.Match;
import info.statstrats.banindex.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
     * Returns a list of all matches in the data store for the given Region.
     */
    public List<Match> getMatches(Region region) throws SQLException {
        checkNotNull(region);
        List<Match> matches = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(
                "select match_id from raw_match_data where region=?")) {
            stmt.setString(1, region.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long matchId = rs.getLong(1);
                    matches.add(new Match(region, matchId));
                }
            }
        }

        return matches;
    }

    /**
     * Returns the JSON blob for the given match.
     *
     * @throws RuntimeException if there is no entry in the data store for the given match
     */
    public String getMatch(Match match) throws SQLException {
        checkNotNull(match);

        try (PreparedStatement stmt = conn.prepareStatement(
                "select json from raw_match_data where region=? and match_id=?")) {
            stmt.setString(1, match.getRegion().toString());
            stmt.setLong(2, match.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new RuntimeException("invalid match: " + match);
                }
                return rs.getString(1);
            }
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

    /**
     * Inserts or replaces champion win/game stats.
     */
    public void replaceChampionWinStats(int championId, int wins, int games) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "replace champion (id, wins, games) values (?,?,?)")) {
            stmt.setInt(1, championId);
            stmt.setInt(2,  wins);
            stmt.setInt(3,  games);
            stmt.execute();
        }
    }

    public List<Champion> retrieveChampionWinStats() throws SQLException {
        List<Champion> champions = new ArrayList<>(124);
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from champion");
            while (rs.next()) {
                champions.add(new Champion(
                        rs.getInt("id"),
                        rs.getInt("wins"),
                        rs.getInt("games")));
            }
        }
        return champions;
    }

    private Timestamp toTimestamp(long date) {
        return new Timestamp(date * 1000);
    }
}
