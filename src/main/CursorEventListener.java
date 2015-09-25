package main;

/**
 * Callbacks for cursor movement on {@link TextEditor}.
 */
public interface CursorEventListener {

    /**
     * Callback to be used when the cursor is moved right.
     */
    void onRightMove();

    /**
     * Callback to be used when the cursor is moved left.
     */
    void onLeftMove();

    /**
     * Callback to be used when the cursor is moved up.
     */
    void onUpMove();

    /**
     * Callback to be used when the cursor is moved down.
     */
    void onDownMove();

    /**
     * Callback to be used when the cursor is moved right with the selection button activated.
     */
    void onSelectionRightMove();

    /**
     * Callback to be used when the cursor is moved left with the selection button activated.
     */
    void onSelectionLeftMove();

    /**
     * Callback to be used when the cursor is moved up with the selection button activated.
     */
    void onSelectionUpMove();

    /**
     * Callback to be used when the cursor is moved down with the selection button activated.
     */
    void onSelectionDownMove();
}
