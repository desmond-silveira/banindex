/**
 * Copyright (c) 2015 Desmond Silveira. All rights reserved.
 */
package info.statstrats.banindex;

import info.statstrats.banindex.dao.ApiDao;
import info.statstrats.banindex.dao.DaoFactory;
import info.statstrats.banindex.dao.RiotDao;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NurfScraper {

    private static final Logger logger = LogManager.getLogger();

    /** 2015-03-31 6:55 UTC or 2015-03-30 23:55 PST8PDT */
//    private static final long START = 1427785200;

    // First non-404 times per region:
    // NA:  1427865900
    private static final long START = 1427865900;

    public static void main(String[] args) throws SQLException {

        DaoFactory daoFactory = new DaoFactory(System.getProperty("DbConnStr"));
        try {
            daoFactory.beginConnectionScope();
        } catch (SQLException e) {
            logger.fatal("Could not create a database connection:  {}", e.getMessage());
            System.exit(1);
        }
        ApiDao api = daoFactory.createApiDao();
        RiotDao riotDao = daoFactory.createRiotDao();

        for (Region region : Region.values()) {
            long beginDate = START;
            do {
                long[] matchIds = api.retrieveNurfMatchIds(region, beginDate);
                if (matchIds != null && matchIds.length > 0) {
                    for (long matchId : matchIds) {
                        Match match = new Match(region, matchId);
                        if (!riotDao.hasMatch(match)) {
                            String json = api.retrieveMatch(match);
                            riotDao.insertMatch(match, json);
                        }
                    }

                    if (!riotDao.hasNurfInterval(region, beginDate)) {
                        riotDao.createAll(region, beginDate, matchIds);
                    }
                }

                beginDate += 300;

            } while (beginDate < System.currentTimeMillis() / 1000);
        }
        try {
            daoFactory.endConnectionScope();
        } catch (SQLException e) {
            logger.fatal("Could not close database connection.");
            System.exit(1);
        }
    }
}
