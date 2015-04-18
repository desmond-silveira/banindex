package info.statstrats.banindex;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import info.statstrats.banindex.dao.DaoFactory;
import info.statstrats.banindex.dao.RiotDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class ChampionBans {

    private List<Champion> champions;

    public ChampionBans() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        }
        DaoFactory daoFactory = new DaoFactory(System.getProperty("DbConnStr"));
        try {
            daoFactory.beginConnectionScope();
            RiotDao riotDao = daoFactory.createRiotDao();
            champions = riotDao.retrieveChampionWinStats();
            Collections.sort(champions);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                daoFactory.endConnectionScope();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<Champion> getChampions() {
        return champions;
    }
}
