package main;

import main.actions.Direction;
import main.actions.MoveCursorAction;
import model.Location;
import model.TextEditorModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * This component is a simple text editor.
 * -- in construction TODO: Update javaDoc.
 */
public class TextEditor extends JComponent implements
        CursorEventListener {

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
    }

    /**
     * Method sets all key bindings for this component.
     */
    private void setKeyBindings() {
        InputMap inputMap = getInputMap();
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "CLOSE");
        actionMap.put("CLOSE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //region CursorMovement
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP);
        //endregion

        //region DeletionSelection
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), BACKSPACE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK), SHIFT_LEFT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), SHIFT_RIGHT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), SHIFT_UP);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), SHIFT_DOWN);
        //endregion

        //region CursorMovementActions
        actionMap.put(RIGHT, new MoveCursorAction.NoSelection(Direction.RIGHT, this));
        actionMap.put(LEFT, new MoveCursorAction.NoSelection(Direction.LEFT, this));
        actionMap.put(DOWN, new MoveCursorAction.NoSelection(Direction.DOWN, this));
        actionMap.put(UP, new MoveCursorAction.NoSelection(Direction.UP, this));
        //endregion

        //region DeletionSelectionActions
        actionMap.put(BACKSPACE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mTextEditorModel.deleteBefore();
            }
        });
        actionMap.put(DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mTextEditorModel.deleteAfter();
            }
        });
        actionMap.put(SHIFT_RIGHT, new MoveCursorAction.Selection(Direction.RIGHT, this));
        actionMap.put(SHIFT_LEFT, new MoveCursorAction.Selection(Direction.LEFT, this));
        actionMap.put(SHIFT_DOWN, new MoveCursorAction.Selection(Direction.DOWN, this));
        actionMap.put(SHIFT_UP, new MoveCursorAction.Selection(Direction.UP, this));
        //endregion

    }

    /**
     * Method displays text from {@link TextEditorModel} for this object.
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        paintTestText(g);
        paintCursor(g);
    }

    /**
     * Method draws a cursor line at the current cursor location.
     *
     * @param g {@link Graphics}.
     */
    private void paintCursor(Graphics g) {
        //        Paint cursor.
        int verticalStep = g.getFontMetrics().getMaxAscent();
        Location cursorLocation = mTextEditorModel.getCursorLocation();
        String subString = mTextEditorModel
                .linesRange(cursorLocation.getY(), cursorLocation.getY() + 1)
                .next()
                .substring(0, cursorLocation.getX());
        int horizontalStep = g.getFontMetrics().stringWidth(subString);

//        System.out.println(cursorLocation.toString());
        g.drawLine(
                horizontalStep + mPadding,
                cursorLocation.getY() * verticalStep + mPadding,
                horizontalStep + mPadding,
                cursorLocation.getY() * verticalStep + verticalStep + mPadding
        );
    }

    /**
     * Method paints test text.
     *
     * @param g {@link Graphics}.
     */
    private void paintTestText(Graphics g) {
        int verticalStep = g.getFontMetrics().getMaxAscent();

//        g.setColor(Color.blue);
//        g.fillRect(
//                mPadding,
//                verticalStep + mPadding + 2,
//                50,
//                -verticalStep
//        );
//        g.setColor(Color.black);

        Point writingCoordinates = new Point(
                mPadding,
                verticalStep + mPadding
        );
        mTextEditorModel.allLines().forEachRemaining(s -> {
            g.drawString(s, writingCoordinates.x, writingCoordinates.y);
            writingCoordinates.y += verticalStep;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRightMove() {
        mTextEditorModel.moveCursorRight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLeftMove() {
        mTextEditorModel.moveCursorLeft();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpMove() {
        mTextEditorModel.moveCursorUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDownMove() {
        mTextEditorModel.moveCursorDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionRightMove() {
        System.out.println("Selection right move");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionLeftMove() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionUpMove() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelectionDownMove() {

    }
}
