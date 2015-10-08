package main;

/**
 * Event listener for copy paste actions.
 */
public interface CopyPasteEventListener {
    void onCopy();
    void onCut();
    void onPeekPaste();
    void onPopPaste();
}
