package main.actions;

import main.CopyPasteEventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *
 */
public class CopyPasteActions {
    public static class Copy extends AbstractAction {
        CopyPasteEventListener mListener;

        public Copy(CopyPasteEventListener listener) {
            mListener = listener;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mListener.onCopy();
        }
    }

    public static class Cut extends AbstractAction {
        CopyPasteEventListener mListener;

        public Cut(CopyPasteEventListener listener) {
            mListener = listener;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mListener.onCut();
        }
    }

    public static class PeekPaste extends AbstractAction {
        CopyPasteEventListener mListener;

        public PeekPaste(CopyPasteEventListener listener) {
            mListener = listener;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mListener.onPeekPaste();
        }
    }

    public static class PopPaste extends AbstractAction {
        CopyPasteEventListener mListener;

        public PopPaste(CopyPasteEventListener listener) {
            mListener = listener;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mListener.onPopPaste();
        }
    }
}
