package model;

/**
 * Encapsulates selection for {@link TextEditorModel}.
 * Selection begins with start {@link Location} and ends with end {@link Location}.
 *
 * @see Location
 */
public class LocationRange {

    private Location mStart;
    private Location mEnd;

    /**
     * Creates a location range with default start and end {@link Location}s.
     * Default {@link Location}s are initiated with zeroes.
     */
    public LocationRange() {
        mStart = new Location();
        mEnd = new Location();
    }

    /**
     * Creates a location range with given start and end {@link Location}s.
     *
     * @param start beginning of the range.
     * @param end   end of the range.
     */
    public LocationRange(Location start, Location end) {
        mStart = start;
        mEnd = end;
    }

    /**
     * Creates another location range from given location range.
     *
     * @param range another location range.
     */
    public LocationRange(LocationRange range) {
        mStart = range.getStart();
        mEnd = range.getEnd();
    }

    /**
     * Creates a location range from four integer parameters.
     * Two parameters create a starting and an ending {@link Location}.
     *
     * @param x1 x parameter for a starting {@link Location}.
     * @param y1 y parameter for a starting {@link Location}.
     * @param x2 x parameter for an ending {@link Location}.
     * @param y2 y parameter for an ending {@link Location}.
     */
    public LocationRange(int x1, int y1, int x2, int y2) {
        mStart = new Location(x1, y1);
        mEnd = new Location(x2, y2);
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

    /**
     * Orders start and end so that end is bottom right and start is upper left.
     * Regarding that order it returns start.
     * WARNING: This method may or may not return the same location as {@link LocationRange#getStart()}.
     *
     * @return {@link Location}.
     */
    public Location getBottomRightStart() {
        if (isEndBottomRight()) {
            return mStart;
        } else {
            return mEnd;
        }
    }

    /**
     * Orders start and end so that end is bottom right and start is upper left.
     * Regarding that order it returns end.
     * WARNING: This method may or may not return the same location as {@link LocationRange#getEnd()}.
     *
     * @return {@link Location}.
     */
    public Location getBottomRightEnd() {
        if (isEndBottomRight()) {
            return mEnd;
        } else {
            return mStart;
        }
    }

    /**
     * Method tells whether the end component of the range is bottom-right
     * relative to its start.
     *
     * @return boolean, true if the end component is located bottom-right relative to its start.
     */
    private boolean isEndBottomRight() {
        int deltaY = this.getEnd().getY() - this.getStart().getY();
        int deltaX = this.getEnd().getX() - this.getStart().getX();
        return deltaY > 0 || (deltaY == 0 && deltaX > 0);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link String}.
     */
    @Override
    public String toString() {
        return mStart.toString() + ".." + mEnd.toString();
    }
}
