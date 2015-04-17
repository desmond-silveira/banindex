package info.statstrats.banindex.model.riotapi.match;

import java.util.List;
import java.util.Map;

public final class Frame {

    private List<Event> events;
    private Map<String, ParticipantFrame> participantFrames;
    private long timestamp;
}
