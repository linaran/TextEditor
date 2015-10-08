package main;

import main.actions.CopyPasteActions;
import main.actions.Direction;
import main.actions.MoveCursorAction;
import model.ClipboardStack;
import model.Location;
import model.LocationRange;
import model.TextEditorModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This component is a simple text editor.
 * -- in construction TODO: Update javaDoc.
 */
public class TextEditor extends JComponent implements
        CursorEventListener, KeyListener, CopyPasteEventListener {

    private static final int COMP_WIDTH = 500;
    private static final int COMP_HEIGHT = 500;
    private static final int mPadding = 10;

    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String DOWN = "down";
    private static final String UP = "up";
    private static final String BACKSPACE = "backspace";
    private static final String DELETE = "delete";
    private static final String SHIFT_LEFT = "shift_left";
    private static final String SHIFT_RIGHT = "shift_right";
    private static final String SHIFT_UP = "shift_up";
    private static final String SHIFT_DOWN = "shift_down";
    private static final String CONTROL_C = "control_C";
    private static final String CONTROL_X = "control_x";
    private static final String CONTROL_V = "control_v";
    private static final String SHIFT_CONTROL_V = "shift_control_v";

    private TextEditorModel mTextEditorModel;

    /**
     * Constructor for {@link TextEditor}.
     * Preferred size is 500 (pixels I guess).
     * Displays test text for now.
     */
    public TextEditor() {
//        Set key bindings.
        setKeyBindings();

        //Sets preferred size of the editor.
        setPreferredSize(new Dimension(COMP_WIDTH, COMP_HEIGHT));

        String initialText = "Hello world!\n" +
                "Good to see you again!\n" +
                "How do you do!";
        mTextEditorModel = new TextEditorModel(initialText);

        //What to do when cursor location changes.
        mTextEditorModel.addCursorObserver(loc -> repaint());
        mTextEditorModel.addTextObserver(this::repaint);
    }

    /**
     * Method sets all key bindings for this component.
     */
    private void setKeyBindings() {
        InputMap inputMap = getInputMap();
        ActionMap actionMap = getActionMap();

        //region CursorMovement
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP);
        //endregion

        //region CursorMovementActions
        actionMap.put(RIGHT, new MoveCursorAction.NoSelection(Direction.RIGHT, this));
        actionMap.put(LEFT, new MoveCursorAction.NoSelection(Direction.LEFT, this));
        actionMap.put(DOWN, new MoveCursorAction.NoSelection(Direction.DOWN, this));
        actionMap.put(UP, new MoveCursorAction.NoSelection(Direction.UP, this));
        //endregion

        //region DeletionSelection
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), BACKSPACE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK), SHIFT_LEFT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), SHIFT_RIGHT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), SHIFT_UP);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), SHIFT_DOWN);
        //endregion

        //region DeletionSelectionActions
        actionMap.put(BACKSPACE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final LocationRange selection = mTextEditorModel.getSelectionRange();
                if (selection != null) {
                    mTextEditorModel.deleteRange(selection);
                } else {
                    mTextEditorModel.deleteBefore();
                }
            }
        });
        actionMap.put(DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final LocationRange selection = mTextEditorModel.getSelectionRange();
                if (selection != null) {
                    mTextEditorModel.deleteRange(selection);
                } else {
                    mTextEditorModel.deleteAfter();
                }
            }
        });
        actionMap.put(SHIFT_RIGHT, new MoveCursorAction.Selection(Direction.RIGHT, this));
        actionMap.put(SHIFT_LEFT, new MoveCursorAction.Selection(Direction.LEFT, this));
        actionMap.put(SHIFT_DOWN, new MoveCursorAction.Selection(Direction.DOWN, this));
        actionMap.put(SHIFT_UP, new MoveCursorAction.Selection(Direction.UP, this));
        //endregion

        //region CopyPasteStuff
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), CONTROL_C);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), CONTROL_X);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), CONTROL_V);
        inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK), SHIFT_CONTROL_V
        );
        //endregion

        //region CopyPasteActions
        actionMap.put(CONTROL_C, new CopyPasteActions.Copy(this));
        actionMap.put(CONTROL_X, new CopyPasteActions.Cut(this));
        actionMap.put(CONTROL_V, new CopyPasteActions.PeekPaste(this));
        actionMap.put(SHIFT_CONTROL_V, new CopyPasteActions.PopPaste(this));
        //endregion

        addKeyListener(this);
    }

    /**
     * Method displays text from {@link TextEditorModel} for this object.
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int verticalStep = g.getFontMetrics().getMaxAscent();

        //region SelectionPaint
        final LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        if (selectionRange != null) {
            Location start = selectionRange.getBottomRightStart();
            Location end = selectionRange.getBottomRightEnd();

            if (start.getY() == end.getY()) {
                //region RectForSameLine
                final String substringSelection = mTextEditorModel.getLine(start.getY())
                        .substring(start.getX(), end.getX());
                final String substringStart = mTextEditorModel.getLine(start.getY())
                        .substring(0, start.getX());
                final int substringStartWidth = g.getFontMetrics().stringWidth(substringStart);

                g.setColor(Color.orange);
                g.fillRect(
                        substringStartWidth + mPadding,
                        start.getY() * verticalStep + mPadding,
                        g.getFontMetrics().stringWidth(substringSelection),
                        verticalStep + 2
                );
                g.setColor(Color.black);
                //endregion
            } else {
                //region RectForStart
                final String substringStartPos = mTextEditorModel.getLine(start.getY())
                        .substring(0, start.getX());
                final String startSubstring = mTextEditorModel.getLine(start.getY())
                        .substring(start.getX(), mTextEditorModel.getLine(start.getY()).length());
                final int substringStartWidth = g.getFontMetrics().stringWidth(substringStartPos);

                g.setColor(Color.orange);
                g.fillRect(
                        substringStartWidth + mPadding,
                        start.getY() * verticalStep + mPadding,
                        g.getFontMetrics().stringWidth(startSubstring),
                        verticalStep + 2
                );
                g.setColor(Color.black);
                //endregion

                //region RectForBetween
                for (int i = start.getY() + 1; i < end.getY(); i++) {
                    final String line = mTextEditorModel.getLine(i);
                    final int width = g.getFontMetrics().stringWidth(line);
                    g.setColor(Color.orange);
                    g.fillRect(
                            mPadding,
                            i * verticalStep + mPadding,
                            width,
                            verticalStep + 2
                    );
                    g.setColor(Color.black);
                }
                //endregion

                //region RectForEnd
                final String substringEnd = mTextEditorModel.getLine(end.getY())
                        .substring(0, end.getX());
                final int substringEndWidth = g.getFontMetrics().stringWidth(substringEnd);

                g.setColor(Color.orange);
                g.fillRect(
                        mPadding,
                        end.getY() * verticalStep + mPadding,
                        substringEndWidth,
                        verticalStep + 2
                );
                g.setColor(Color.black);
                //endregion
            }
        }
        //endregion

        //region cursorPaint
        final Location cursorLocation = mTextEditorModel.getCursorLocation();
        final String cursorSubstring = mTextEditorModel.getLine(cursorLocation.getY())
                .substring(0, cursorLocation.getX());
        final int cursorSubstringWidth = g.getFontMetrics().stringWidth(cursorSubstring);
//        System.out.println(cursorLocation.toString());
        g.drawLine(
                cursorSubstringWidth + mPadding,
                cursorLocation.getY() * verticalStep + mPadding,
                cursorSubstringWidth + mPadding,
                (cursorLocation.getY() * verticalStep + mPadding) + verticalStep
        );
        //endregion

        //region testText
        Point writingCoordinates = new Point(
                mPadding,
                verticalStep + mPadding
        );
        mTextEditorModel.allLines().forEachRemaining(s -> {
            g.drawString(s, writingCoordinates.x, writingCoordinates.y);
            writingCoordinates.y += verticalStep;
        });
        //endregion
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRightMove() {
        LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        Location cursorLocation = mTextEditorModel.getCursorLocation();

        if (selectionRange != null) {
            cursorLocation.setLocation(selectionRange.getBottomRightEnd());
            repaint();
            mTextEditorModel.setSelectionRange(null);
        } else {
            mTextEditorModel.moveCursorRight();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLeftMove() {
        LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        Location cursorLocation = mTextEditorModel.getCursorLocation();

        if (selectionRange != null) {
            cursorLocation.setLocation(selectionRange.getBottomRightStart());
            repaint();
            mTextEditorModel.setSelectionRange(null);
        } else {
            mTextEditorModel.moveCursorLeft();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpMove() {
        mTextEditorModel.moveCursorUp();
        mTextEditorModel.setSelectionRange(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDownMove() {
        mTextEditorModel.moveCursorDown();
        mTextEditorModel.setSelectionRange(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionRightMove() {
        LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        if (selectionRange == null) {
            Location cursorLocation = mTextEditorModel.getCursorLocation();
            selectionRange = new LocationRange(
                    new Location(cursorLocation),
                    cursorLocation
            );
            mTextEditorModel.setSelectionRange(selectionRange);
            mTextEditorModel.moveCursorRight();
        } else {
            mTextEditorModel.moveCursorRight();
        }
//        System.out.println(selectionRange.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionLeftMove() {
        LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        if (selectionRange == null) {
            Location cursorLocation = mTextEditorModel.getCursorLocation();
            selectionRange = new LocationRange(
                    new Location(cursorLocation),
                    cursorLocation
            );
            mTextEditorModel.setSelectionRange(selectionRange);
            mTextEditorModel.moveCursorLeft();
        } else {
            mTextEditorModel.moveCursorLeft();
        }
//        System.out.println(selectionRange.toString());
    }

    @Override
    public void onCopy() {
        if (mTextEditorModel.getSelectionRange() != null) {
            mTextEditorModel.getClipboardStack().push(
                    mTextEditorModel.selectionToString(
                            mTextEditorModel.getSelectionRange()
                    )
            );
        }
    }

    @Override
    public void onCut() {
        if (mTextEditorModel.getSelectionRange() != null) {
            mTextEditorModel.getClipboardStack().push(
                    mTextEditorModel.selectionToString(
                            mTextEditorModel.getSelectionRange()
                    )
            );
            mTextEditorModel.deleteRange(mTextEditorModel.getSelectionRange());
        }
    }

    @Override
    public void onPeekPaste() {
        ClipboardStack clipboardStack = mTextEditorModel.getClipboardStack();
        if (!clipboardStack.isEmpty()){
            mTextEditorModel.insert(
                    clipboardStack.peek()
            );
        }
    }

    @Override
    public void onPopPaste() {
        ClipboardStack clipboardStack = mTextEditorModel.getClipboardStack();
        if (!clipboardStack.isEmpty()) {
            mTextEditorModel.insert(
                    clipboardStack.pop()
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionUpMove() {
        LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        if (selectionRange == null) {
            Location cursorLocation = mTextEditorModel.getCursorLocation();
            selectionRange = new LocationRange(
                    new Location(cursorLocation),
                    cursorLocation
            );
            mTextEditorModel.setSelectionRange(selectionRange);
            mTextEditorModel.moveCursorUp();
        } else {
            mTextEditorModel.moveCursorUp();
        }
//        System.out.println(selectionRange.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionDownMove() {
        LocationRange selectionRange = mTextEditorModel.getSelectionRange();
        if (selectionRange == null) {
            Location cursorLocation = mTextEditorModel.getCursorLocation();
            selectionRange = new LocationRange(
                    new Location(cursorLocation),
                    cursorLocation
            );
            mTextEditorModel.setSelectionRange(selectionRange);
            mTextEditorModel.moveCursorDown();
        } else {
            mTextEditorModel.moveCursorDown();
        }
//        System.out.println(selectionRange.toString());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        LocationRange selection = mTextEditorModel.getSelectionRange();
        boolean asciiFilter = c != 8 &&
                c != 127 &&
                (e.getModifiers() == 0 || e.getModifiers() == InputEvent.SHIFT_MASK);
//        c != 8 && c != 127
        if (asciiFilter) {
            if (selection != null) {
                mTextEditorModel.deleteRange(selection);
            }
            mTextEditorModel.insert(c);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
