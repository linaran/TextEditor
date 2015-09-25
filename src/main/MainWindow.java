package main;

import javax.swing.*;

/**
 * TODO: Javadoc.
 */
public class MainWindow {

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TextEditor textEditor = new TextEditor();
        frame.add(textEditor);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(MainWindow::createAndShowGui);
    }

}
