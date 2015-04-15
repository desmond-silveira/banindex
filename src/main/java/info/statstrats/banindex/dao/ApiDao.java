package info.statstrats.banindex.dao;

import info.statstrats.banindex.Match;
import info.statstrats.banindex.Region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Charsets;

import com.google.gson.Gson;

public class ApiDao {

    private static final Logger logger = LogManager.getLogger();

    private final Gson gson = new Gson();

    private static final String API_CHALLENGE_URL =
            "https://na.api.pvp.net/api/lol/%s/v4.1/game/ids?beginDate=%d&api_key="
            + System.getProperty("RiotKey");

    /**
     * Fetches the NURF match ids for the given Region and date interval.
     *
     * @return null if an error occurred
     */
    public long[] retrieveNurfMatchIds(Region region, long beginDate) {
        try {
            URL url = new URL(String.format(API_CHALLENGE_URL, region.toString(), beginDate));
            String response = get(url);
            if (response != null) {
                return gson.fromJson(response, long[].class);
            }
            return null;
        } catch (MalformedURLException e) {
            logger.error(e);
            return null;
        }
    }

    private static final String MATCH_URL =
            "https://na.api.pvp.net/api/lol/%s/v2.2/match/%d?api_key="
                + System.getProperty("RiotKey");

    /**
     * Fetches the indicated match data and returns its JSON blob.
     *
     * @return null if an error occurred
     */
    public String retrieveMatch(Match match) {
        try {
            URL url = new URL(String.format(MATCH_URL,
                    match.getRegion(), match.getId()));
            return get(url);
        } catch (MalformedURLException e) {
            logger.catching(e);
            return null;
        }
    }

    /**
     * Gets the response {@code String} for the given {@code URL}.
     *
     * @param url the {@code URL}
     * @return the response {@code String}
     */
    static String get(URL url) {
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
            logger.warn("HTTP {}:  {}\n", status, url);
            return null;
        }
        String response = sb.toString();
        logger.trace(response);
        return response;
    }
}
