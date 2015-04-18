package info.statstrats.banindex;

import com.google.common.base.MoreObjects;

public class Champion implements Comparable<Champion> {
    private int id;
    private int wins;
    private int games;
    private double banIndex;

    public Champion(int id, int wins, int games) {
        this.id = id;
        this.wins = wins;
        this.games = games;
        this.banIndex = (getWinRate() - .5) * games;
    }

    public int getId() {
        return id;
    }

    public int getWins() {
        return wins;
    }

    public double getWinRate() {
        return (double) wins / games;
    }

    public int getGames() {
        return games;
    }

    public double getBanIndex() {
        return banIndex;
    }

    public String getImageName() {
        return ChampionNameMap.INSTANCE.getName(id).toLowerCase().replaceAll("\\W", "");
    }

    @Override
    public int compareTo(Champion champion) {
        return Double.compare(champion.getBanIndex(), this.getBanIndex());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("wins", wins)
                .add("games", games)
                .add("banIndex", banIndex)
                .toString();
    }
}
