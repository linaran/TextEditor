package model;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 *
 */
public class ClipboardStack {
    private Stack<String> mTexts;
    private Set<ClipboardObserver> mClipboardObservers;

    /**
     * Creates a default (empty) clipboard stack.
     */
    public ClipboardStack() {
        mTexts = new Stack<>();
        mClipboardObservers = new HashSet<>();
    }

    /**
     * Put an element on top of the clipboard stack.
     *
     * @param text {@link String}.
     */
    public void push(String text) {
        mTexts.push(text);
    }

    /**
     * Removes and returns an element from the top of the clipboard stack.
     *
     * @return {@link String}.
     */
    public String pop() {
        return mTexts.pop();
    }

    /**
     * Returns but doesn't remove an element from the top of the clipboard stack.
     * Note: Use {@link ClipboardStack#pop()} to return and remove an element.
     *
     * @return {@link String}.
     */
    public String peek() {
        return mTexts.peek();
    }

    /**
     * Returns true if the clipboard stack is empty.
     *
     * @return primitive boolean.
     */
    public boolean isEmpty() {
        return mTexts.isEmpty();
    }

    /**
     * Method empties the clipboard stack.
     * After this the {@link ClipboardStack#isEmpty()} will return true.
     */
    public void clear() {
        mTexts.clear();
    }

    /**
     * Adds a {@link ClipboardObserver}.
     *
     * @param observer {@link ClipboardObserver}.
     */
    public void addClipboardObserver(ClipboardObserver observer) {
        mClipboardObservers.add(observer);
    }

    /**
     * Removes a {@link ClipboardObserver}.
     *
     * @param observer {@link ClipboardObserver}.
     */
    public void removeClipboardObserver(ClipboardObserver observer) {
        mClipboardObservers.remove(observer);
    }

    /**
     * Updates all {@link ClipboardObserver}s.
     */
    private void updateClipboardObservers() {
        mClipboardObservers.forEach(model.ClipboardObserver::updateClipboard);
    }


}

