package info.statstrats.banindex.model.riotapi.match;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MatchDetail {

    private int mapId;
    private long matchCreation;
    private long matchDuration;
    private long matchId;
    private String matchMode;
    private String matchType;
    private String matchVersion;
    private List<ParticipantIdentity> participantIdentities;
    private List<Participant> participants;
    private String platformId;
    private String queueType;
    private String region;
    private String season;
    private List<Team> teams;
    private Timeline timeline;

    /**
     * Returns the set of champion ids that fought in this match.
     */
    public Set<Integer> getChampions() {
        Set<Integer> championIds = new HashSet<>();
        for (Participant participant : participants) {
            championIds.add(participant.getChampionId());
        }
        return championIds;
    }

    /**
     * Returns the set of champion ids that won this match.
     */
    public Set<Integer> getWinningChampions() {
        Set<Integer> winningTeams = new HashSet<>();
        for (Team team : teams) {
            if (team.isWinner()) {
                winningTeams.add(team.getTeamId());
            }
        }

        Set<Integer> championIds = new HashSet<>();
        for (Participant participant : participants) {
            int teamId = participant.getTeamId();
            if (winningTeams.contains(teamId)) {
                championIds.add(participant.getChampionId());
            }
        }
        return championIds;
    }
}
