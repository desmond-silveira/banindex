/**
 * Copyright (c) 2015 Desmond Silveira. All rights reserved.
 */
package info.statstrats.banindex;

import info.statstrats.banindex.dao.DaoFactory;
import info.statstrats.banindex.dao.RiotDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Charsets;

import com.google.gson.Gson;

/**
 * @author dsilveira
 */
public class NurfScraper {

    private static final Logger logger = LogManager.getLogger();

    /** 2015-03-31 6:55 UTC or 2015-03-30 23:55 PST8PDT */
//    private static final long START = 1427785200;

    // First non-404 times per region:
    // NA:  1427865900
    private static final long START = 1427865900;

    private static final String API_CHALLENGE_URL =
            "https://na.api.pvp.net/api/lol/%s/v4.1/game/ids?beginDate=%d&api_key="
            + System.getProperty("RiotKey");


    public static void main(String[] args) {

        DaoFactory daoFactory = new DaoFactory(System.getProperty("DbConnStr"));
        try {
            daoFactory.beginConnectionScope();
        } catch (SQLException e) {
            logger.fatal("Could not create a database connection:  {}", e.getMessage());
            System.exit(1);
        }
        RiotDao riotDao = daoFactory.createRiotDao();
        Gson gson = new Gson();

//        for (Region region : Region.values()) {
        Region region = Region.NA;
            long beginDate = START;
            do {
                try {
                    URL url = new URL(String.format(API_CHALLENGE_URL,
                            region.toString().toLowerCase(), beginDate));
                    String response = get(url);
                    long[] matches = gson.fromJson(response, long[].class);
                    if (matches.length > 0) {
                        try {
                            riotDao.createAll(region, beginDate, matches);
                        } catch (SQLException e) {
                            logger.catching(e);
                        }
                    }
                    beginDate += 300;
                } catch (MalformedURLException e) {
                    logger.catching(e);
                }
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    logger.catching(e);
                    Thread.interrupted();
                    return;
                }
            } while (beginDate < System.currentTimeMillis() / 1000);

//        }
        try {
            daoFactory.endConnectionScope();
        } catch (SQLException e) {
            logger.fatal("Could not close database connection.");
            System.exit(1);
        }
    }

    /**
     * Gets the response {@code String} for the given {@code URL}.
     *
     * @param url the {@code URL}
     * @return the response {@code String}
     */
    public static String get(URL url) {
        logger.debug(url.toString());
        StringBuilder sb = new StringBuilder();
        int status = 0;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            status = conn.getResponseCode();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), Charsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (IOException e) {
            logger.warn("HTTP {}:  {}", status, url);
        }
        String response = sb.toString();
        logger.trace(response);
        return response;
    }


}
