package model;

/**
 * Encapsulates selection for {@link TextEditorModel}.
 */
public class LocationRange {

    private Location mStart;
    private Location mEnd;

    public LocationRange() {
        mStart = new Location();
        mEnd = new Location();
    }

    public LocationRange(Location start, Location end) {
        mStart = start;
        mEnd = end;
    }

    public Location getStart() {
        return mStart;
    }

    public void setStart(Location start) {
        mStart = start;
    }

    public Location getEnd() {
        return mEnd;
    }

    public void setEnd(Location end) {
        mEnd = end;
    }
}
