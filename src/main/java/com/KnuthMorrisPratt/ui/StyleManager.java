package com.KnuthMorrisPratt.ui;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class StyleManager {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Color SELECTED_COLOR = Color.ORANGE;
    private static final Color FOUND_COLOR = Color.BLUE;
    private static final Color ACTIVE_COLOR = new Color(0xff5900);

    private final StyledDocument doc;
    private final Style defaultStyle;
    private final Style selectedStyle;
    private final Style foundStyle;
    private final Style activeStyle;

    public StyleManager(StyledDocument styledDocument) {
        doc = styledDocument;
        defaultStyle = doc.addStyle("Default", null);
        selectedStyle = doc.addStyle("Selected", null);
        foundStyle = doc.addStyle("Found", null);
        activeStyle = doc.addStyle("Active", null);

        StyleConstants.setForeground(defaultStyle, DEFAULT_COLOR);
        StyleConstants.setForeground(selectedStyle, SELECTED_COLOR);
        StyleConstants.setForeground(foundStyle, FOUND_COLOR);
        StyleConstants.setForeground(activeStyle, ACTIVE_COLOR);
    }

    public void setDefault(int start, int end) {
        doc.setCharacterAttributes(start, end - start, defaultStyle, true);
    }

    public void setFound(int start, int end) {
        doc.setCharacterAttributes(start, end - start, foundStyle, true);
    }

    public void setSelected(int start, int end) {
        doc.setCharacterAttributes(start, end - start, selectedStyle, true);
    }

    public void setActive(int start, int end) {
        doc.setCharacterAttributes(start, end - start, activeStyle, true);
    }
}
