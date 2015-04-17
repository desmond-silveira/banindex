package info.statstrats.banindex;

import info.statstrats.banindex.dao.DaoFactory;
import info.statstrats.banindex.dao.RiotDao;
import info.statstrats.banindex.model.riotapi.match.MatchDetail;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class ProcessChampionWinStats {

    private static final Logger logger = LogManager.getLogger();

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws SQLException {

        DaoFactory daoFactory = new DaoFactory(System.getProperty("DbConnStr"));
        try {
            daoFactory.beginConnectionScope();
        } catch (SQLException e) {
            logger.fatal("Could not create a database connection:  {}", e.getMessage());
            System.exit(1);
        }
        RiotDao riotDao = daoFactory.createRiotDao();

        Map<Integer, Integer> pickedCountByChampion = new HashMap<>();
        Map<Integer, Integer> gamesWonByChampion = new HashMap<>();

        for (Region region : Region.values()) {
            List<Match> matches = riotDao.getMatches(region);
            for (Match match : matches) {
                String json = riotDao.getMatch(match);
                MatchDetail matchDetail = gson.fromJson(json, MatchDetail.class);
                Set<Integer> champions = matchDetail.getChampions();
                Set<Integer> winningChampions = matchDetail.getWinningChampions();
                logger.debug("{}: picked: {}, won: {}", match, champions, winningChampions);

                for (Integer picked : champions) {
                    Integer count = pickedCountByChampion.get(picked);
                    if (count == null) {
                        count = 0;
                    }
                    pickedCountByChampion.put(picked, ++count);
                }

                for (Integer won : winningChampions) {
                    Integer count = gamesWonByChampion.get(won);
                    if (count == null) {
                        count = 0;
                    }
                    gamesWonByChampion.put(won, ++count);
                }
            }
        }

        for (int championId : pickedCountByChampion.keySet()) {
            int picked = pickedCountByChampion.get(championId);
            Integer won = gamesWonByChampion.get(championId);
            if (won == null) {
                won = 0;
                logger.debug("Mental note: never EVER pick champion {} in a ranked game.", championId);
            }
            riotDao.replaceChampionWinStats(championId, won, picked);
            logger.debug("Updated champion {}: {} wins out of {} games: {}%", championId, won, picked, 100.0 * won / picked);
        }

        try {
            daoFactory.endConnectionScope();
        } catch (SQLException e) {
            logger.fatal("Could not close database connection.");
            System.exit(1);
        }
    }
}
