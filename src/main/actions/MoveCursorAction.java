package main.actions;

import main.CursorEventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class encapsulates cursor movement actions.
 * Depending on given {@link Direction} an instance of this class
 * will move the cursor left, right, up or down.
 * Movements caused by keyboard.
 */
public class MoveCursorAction {

    public static class NoSelection extends AbstractAction {
        Direction mDirection;
        CursorEventListener mCursorEventListener;

        public NoSelection(Direction direction, CursorEventListener cursorEventListener) {
            mDirection = direction;
            mCursorEventListener = cursorEventListener;
        }

        /**
         * Method performs cursor movement based on the direction given in the
         * constructor.
         *
         * @param e {@link ActionEvent}.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (mDirection) {
                case RIGHT:
                    mCursorEventListener.onRightMove();
                    break;
                case LEFT:
                    mCursorEventListener.onLeftMove();
                    break;
                case UP:
                    mCursorEventListener.onUpMove();
                    break;
                case DOWN:
                    mCursorEventListener.onDownMove();
                    break;
                default:
                    throw new EnumConstantNotPresentException(Direction.class, "Enum not present");
            }
        }
    }

    public static class Selection extends AbstractAction {
        Direction mDirection;
        CursorEventListener mCursorEventListener;

        public Selection(Direction direction, CursorEventListener cursorEventListener) {
            mCursorEventListener = cursorEventListener;
            mDirection = direction;
        }

        /**
         * Method performs cursor movement based on the direction given in the
         * constructor. Also updates selection of the currently selected class.
         *
         * @param e {@link ActionEvent}.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (mDirection) {
                case RIGHT:
                    mCursorEventListener.onSelectionRightMove();
                    break;
                case LEFT:
                    mCursorEventListener.onSelectionLeftMove();
                    break;
                case UP:
                    mCursorEventListener.onSelectionUpMove();
                    break;
                case DOWN:
                    mCursorEventListener.onSelectionDownMove();
                    break;
                default:
                    throw new EnumConstantNotPresentException(Direction.class, "Enum not present");
            }
        }
    }
}
