package info.statstrats.banindex;

public class Match {

    final Region region;
    final long id;

    public Match(Region region, long id) {
        this.region = region;
        this.id = id;
    }

    public Region getRegion() {
        return region;
    }

    public long getId() {
        return id;
    }
}
