package info.statstrats.banindex.model.riotapi.match;

import java.util.List;

public final class Participant {

    private int championId;
    private String highestAchievedSeasonTier;
    private List<Mastery> masteries;
    private int participantId;
    private List<Rune> runes;
    private int spell1Id;
    private int spell2Id;
    private ParticipantStats stats;
    private int teamId;
    private ParticipantTimeline timeline;

    public int getChampionId() {
        return championId;
    }

    public int getTeamId() {
        return teamId;
    }
}
