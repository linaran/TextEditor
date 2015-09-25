package model;

/**
 * Cursor observer interface.
 */
public interface CursorObserver {

    /**
     * Method for updating cursor location.
     *
     * @param loc {@link Location}
     */
    void updateCursorLocation(Location loc);

}
