package info.statstrats.banindex.model.riotapi.match;

import java.util.List;

public final class Team {

    private List<BannedChampion> bans;
    private int baronKills;
    private long dominionVictoryScore;
    private int dragonKills;
    private boolean firstBaron;
    private boolean firstBlood;
    private boolean firstDragon;
    private boolean firstInhibitor;
    private boolean firstTower;
    private int inhibitorKills;
    private int teamId;
    private int towerKills;
    private int vilemawKills;
    private boolean winner;

    public int getTeamId() {
        return teamId;
    }

    public boolean isWinner() {
        return winner;
    }
}
