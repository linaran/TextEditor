package model;

import java.util.*;

/**
 * Under construction.
 */
public class TextEditorModel {

    private List<String> mLines;
    private Location mCursorLocation;
    /**
     * Can be null.
     */
    private LocationRange mSelectionRange;

    private Set<CursorObserver> mCursorObservers;
    private Set<TextObserver> mTextObservers;

    public TextEditorModel(String text) {
        mLines = new ArrayList<>(Arrays.asList(text.split("\n")));
        mCursorObservers = new HashSet<>();
        mTextObservers = new HashSet<>();
        mCursorLocation = new Location();
    }

    /**
     * Returns cursor location.
     *
     * @return {@link Location}.
     */
    public Location getCursorLocation() {
        return mCursorLocation;
    }

    /**
     * Method returns current text selection range.
     * If the selection didn't exist previously this method will return null.
     *
     * @return {@link LocationRange} which can be null if the selection didn't previously exist.
     */
    public LocationRange getSelectionRange() {
        return mSelectionRange;
    }

    /**
     * Method sets a selection range for the text that was selected.
     *
     * @param selectionRange {@link LocationRange}.
     */
    public void setSelectionRange(LocationRange selectionRange) {
        mSelectionRange = selectionRange;
    }

    /**
     * Method returns string lines contained in this object.
     *
     * @return {@link List<String>}.
     */
    public List<String> getLines() {
        return mLines;
    }

    /**
     * Method returns {@link Iterator<String>} for text contained in this object.
     * Every iteration will return a single line of text in this object.
     *
     * @return {@link Iterator<String>}.
     */
    public Iterator<String> allLines() {
        return mLines.iterator();
    }

    /**
     * Method returns {@link Iterator<String>} for text contained in this object.
     * This iterator won't iterate through every line of text contained in this object.
     * Iterator will only iterate from (inclusive) line given by first parameter to the (exclusive)
     * line given by second parameter.
     *
     * @param index1 int - Starting line of iteration (inclusive).
     * @param index2 int - Ending line of iteration (exclusive).
     * @return {@link Iterator<String>}.
     */
    public Iterator<String> linesRange(int index1, int index2) {
        List<String> slicedList = mLines.subList(index1, index2);
        return slicedList.iterator();
    }

    /**
     * Returns text line (String) located at the given parameter.
     * Parameter starts at 0.
     *
     * @param index primitive integer.
     * @return {@link String}.
     */
    public String getLine(int index) {
        if (index < 0 || index > mLines.size() - 1) {
            throw new UnsupportedOperationException("Given parameter is outside of boundaries.");
        }

        return mLines.get(index);
    }

    /**
     * Method adds a {@link CursorObserver} to {@link TextEditorModel}.
     * Duplicate observers are not allowed.
     *
     * @param observer {@link CursorObserver}.
     */
    public void addCursorObserver(CursorObserver observer) {
        mCursorObservers.add(observer);
    }

    /**
     * Method removes a {@link CursorObserver} from {@link TextEditorModel}.
     *
     * @param observer {@link CursorObserver}.
     */
    public void removeCursorObserver(CursorObserver observer) {
        mCursorObservers.remove(observer);
    }

    /**
     * Method updates all cursor observers on the cursor location.
     */
    private void updateCursorObservers() {
        mCursorObservers.forEach(cursorObserver ->
                cursorObserver.updateCursorLocation(mCursorLocation));
    }

    /**
     * Method moves the location of the cursor left.
     */
    public void moveCursorLeft() {
        try {
            mCursorLocation.setLocation(getLeftLocation(mCursorLocation));
        } catch (UnsupportedOperationException e) {
            return;
        }

        updateCursorObservers();
    }

    /**
     * Returns the first left location, relative to the given parameter.
     *
     * @param currentLocation location to be used as a relative point.
     * @return {@link Location}
     * @throws UnsupportedOperationException if the cursor doesn't have a left location.
     */
    public Location getLeftLocation(Location currentLocation) {
        if (mCursorLocation.getY() == 0 && mCursorLocation.getX() == 0) {
            throw new UnsupportedOperationException("Left location doesn't exist.");
        }

        int x = currentLocation.getX();
        int y = currentLocation.getY();

        if (x == 0) {
            return new Location(
                    mLines.get(y - 1).length(),
                    y - 1
            );
        } else {
            return new Location(
                    x - 1,
                    y
            );
        }
    }

    /**
     * Method moves the location of the cursor right.
     */
    public void moveCursorRight() {
        try {
            mCursorLocation.setLocation(getRightLocation(mCursorLocation));
        } catch (UnsupportedOperationException e) {
            return;
        }

        updateCursorObservers();
    }

    /**
     * Returns the first right location relative to the given parameter location.
     *
     * @param currentLocation location to be used as a relative point.
     * @return {@link Location}.
     * @throws UnsupportedOperationException if the location to the right doesn't exist.
     */
    public Location getRightLocation(Location currentLocation) {
        int lastIndexOnPage = mLines.get(mLines.size() - 1).length();
        if (mCursorLocation.getY() == mLines.size() - 1 && mCursorLocation.getX() == lastIndexOnPage) {
            throw new UnsupportedOperationException("Right location doesn't exist.");
        }

        int x = currentLocation.getX();
        int y = currentLocation.getY();

        int lastIndexInLine = mLines.get(y).length();
        if (x == lastIndexInLine) {
            return new Location(
                    0,
                    y + 1
            );
        } else {
            return new Location(
                    x + 1,
                    y
            );
        }
    }

    /**
     * Method moves the location of the cursor up.
     */
    public void moveCursorUp() {
        try {
            mCursorLocation.setLocation(getUpLocation(mCursorLocation));
        } catch (UnsupportedOperationException e) {
            return;
        }

        updateCursorObservers();
    }

    /**
     * Returns the first up location relative to the cursor location.
     *
     * @return {@link Location}.
     * @throws UnsupportedOperationException if the location doesn't exist.
     */
    public Location getUpLocation(Location currentLocation) {
        int x = currentLocation.getX();
        int y = currentLocation.getY();

        if (y == 0) {
            throw new UnsupportedOperationException("Up location doesn't exist.");
        }

        int aboveLineLastIndex = mLines.get(y - 1).length();
        if (x > aboveLineLastIndex) {
            return new Location(
                    aboveLineLastIndex,
                    y - 1
            );
        } else {
            return new Location(
                    x,
                    y - 1
            );
        }
    }

    /**
     * Method moves the location of the cursor down.
     */
    public void moveCursorDown() {
        try {
            mCursorLocation.setLocation(getDownLocation(mCursorLocation));
        } catch (UnsupportedOperationException e) {
            return;
        }

        updateCursorObservers();
    }

    /**
     * Returns the first down location relative to the cursor location.
     *
     * @return {@link Location}.
     * @throws UnsupportedOperationException if the location doesn't exist.
     */
    public Location getDownLocation(Location currentLocation) {
        int x = currentLocation.getX();
        int y = currentLocation.getY();

        if (y == mLines.size() - 1) {
            throw new UnsupportedOperationException("Down location doesn't exist.");
        }

        int belowLineLastIndex = mLines.get(y + 1).length();
        if (x > belowLineLastIndex) {
            return new Location(
                    belowLineLastIndex,
                    y + 1
            );
        } else {
            return new Location(
                    x,
                    y + 1
            );
        }
    }

    /**
     * Method adds an text observer to the text model.
     * Note: Duplicate text observers are not allowed.
     *
     * @param textObserver {@link TextObserver}.
     */
    public void addTextObserver(TextObserver textObserver) {
        mTextObservers.add(textObserver);
    }

    /**
     * Method removes an text observer from the text model.
     *
     * @param textObserver {@link TextObserver}.
     */
    public void removeTextObserver(TextObserver textObserver) {
        mTextObservers.remove(textObserver);
    }

    /**
     * Private method to update all {@link TextObserver}s.
     */
    private void updateTextObservers() {
        mTextObservers.forEach(TextObserver::updateText);
    }

    /**
     * Removes a character behind the cursor and moves the cursor to the left.
     */
    public void deleteBefore() {
        Location leftLocation;
        try {
            leftLocation = getLeftLocation(mCursorLocation);
        } catch (UnsupportedOperationException e) {
            return;
        }

//        Deletion stabilized.
        if (mCursorLocation.getX() != 0) {
            StringBuilder sb = new StringBuilder(mLines.get(leftLocation.getY()));
            sb.deleteCharAt(leftLocation.getX());
            mLines.set(leftLocation.getY(), sb.toString());
        } else {
            mLines.set(leftLocation.getY(), mLines.get(leftLocation.getY()) + mLines.get(mCursorLocation.getY()));
            mLines.remove(mCursorLocation.getY());
        }

        mCursorLocation.setLocation(leftLocation);

        updateTextObservers();
        updateCursorObservers();
    }

    /**
     * Removes a character after the cursor and moves the cursor to the right.
     */
    public void deleteAfter() {
//        Deletion stabilized
        int lastIndexInLine = mLines.get(mCursorLocation.getY()).length();
        if (mCursorLocation.getX() != lastIndexInLine) {
            StringBuilder sb = new StringBuilder(mLines.get(mCursorLocation.getY()));
            sb.deleteCharAt(mCursorLocation.getX());
            mLines.set(mCursorLocation.getY(), sb.toString());
        } else if (mCursorLocation.getY() != mLines.size() - 1) {
            mLines.set(mCursorLocation.getY(), mLines.get(mCursorLocation.getY()) + mLines.get(mCursorLocation.getY() + 1));
            mLines.remove(mCursorLocation.getY() + 1);
        }

        updateTextObservers();
    }

    /**
     * Removes all text selected with the parameter.
     * If the parameter selects something that goes outside the borders of the text
     * IllegalArgumentException will be thrown.
     *
     * @param range {@link LocationRange}, selected text to be deleted.
     * @throws IllegalArgumentException if parameter selects something outside the borders of the text.
     */
    public void deleteRange(LocationRange range) {
        if (!isSelectionLegal(range)) {
            throw new IllegalArgumentException("Selection range is outside the text borders.");
        }
        if (range.getStart().equals(range.getEnd())) {
            return;
        }

        Location start;
        Location end;
        if (range.getStart().getY() != range.getEnd().getY()) {
            if (range.getStart().getY() < range.getEnd().getY()) {
                start = range.getStart();
                end = range.getEnd();
            } else {
                start = range.getEnd();
                end = range.getStart();
            }

            //region StartStringDeletion
            String startString = mLines.get(start.getY());
            if (start.getX() != startString.length()) {
                StringBuilder sb = new StringBuilder(startString);
                sb.delete(start.getX(), startString.length());
                mLines.set(start.getY(), sb.toString());
            }
            //endregion

            //region EndStringDeletion
            String endString = mLines.get(end.getY());
            if (end.getX() != 0) {
                StringBuilder sb = new StringBuilder(endString);
                sb.delete(0, end.getX());
                mLines.set(end.getY(), sb.toString());
            }
            //endregion

            //region RestDeletion
            for (int i = start.getY() + 1; i < end.getY(); i++) {
                mLines.remove(i);
            }
            //endregion
        } else {
            if (range.getStart().getX() < range.getEnd().getX()) {
                start = range.getStart();
                end = range.getEnd();
            } else {
                start = range.getEnd();
                end = range.getStart();
            }

            StringBuilder sb = new StringBuilder(mLines.get(start.getY()));
            sb.delete(start.getX(), end.getX());
            mLines.set(start.getY(), sb.toString());
        }

        mCursorLocation.setLocation(start.getX(), start.getY());

        updateCursorObservers();
        updateTextObservers();
    }

    /**
     * Method checks if given {@link Location} object is legal on current {@link TextEditorModel}.
     * Returns true if location is true.
     *
     * @param location {@link Location}.
     * @return primitive boolean, is true if location is legal.
     */
    private boolean isLocationLegal(Location location) {
        int x = location.getX();
        int y = location.getY();
        int lastXIndexOnPage = mLines.get(mLines.size() - 1).length();
        int lastYIndexOnPage = mLines.size() - 1;

        return !(x < 0 ||
                y < 0 ||
                x > lastXIndexOnPage ||
                y > lastYIndexOnPage
        );
    }

    /**
     * Method checks if the {@link LocationRange} is legal for this {@link TextEditorModel}.
     * Selections are illegal if they select things beyond the borders of the text itself.
     * Method will return true if the selection is legal.
     *
     * @param range {@link LocationRange} selection to be checked.
     * @return primitive boolean, true if the selection is legal.
     */
    private boolean isSelectionLegal(LocationRange range) {
        Location start = range.getStart();
        Location end = range.getEnd();

        return !(!isLocationLegal(start) || !isLocationLegal(end));
    }
}
