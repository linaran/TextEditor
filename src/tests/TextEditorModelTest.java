package tests;

import model.CursorObserver;
import model.TextEditorModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link TextEditorModel}.
 */
public class TextEditorModelTest {

    private TextEditorModel mTextEditorModel;
    private Set<CursorObserver> mCursorObservers;

    @Before
    public void setUp() throws Exception {
        mTextEditorModel = new TextEditorModel("Hello.\nWorld!");
    }

    /**
     * Constructor must split inputting String by new lines.
     */
    @Test
    public void constructor() {
        String[] expected = new String[]{"Hello.", "World!"};
        assertArrayEquals(mTextEditorModel.getLines().toArray(), expected);
    }

//    /**
//     * Method adds a {@link CursorObserver} to {@link TextEditorModel}.
//     * Duplicate observers are not allowed.
//     *
//     * @param observer {@link CursorObserver}.
//     */
//    @Test
//    public void addCursorObserver(CursorObserver observer) {
//        mCursorObservers.add(observer);
//
//        assertTrue(mCursorObservers.contains(observer));
//    }
//
//    /**
//     * Method removes a {@link CursorObserver} from {@link TextEditorModel}.
//     *
//     * @param observer {@link CursorObserver}.
//     */
//    @Test
//    public void removeCursorObserver(CursorObserver observer) {
//        mCursorObservers.remove(observer);
//
//        assertTrue(!mCursorObservers.contains(observer));
//    }
}