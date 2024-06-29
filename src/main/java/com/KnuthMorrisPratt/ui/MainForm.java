package com.KnuthMorrisPratt.ui;

import com.KnuthMorrisPratt.logic.Automaton;
import com.KnuthMorrisPratt.logic.Searcher;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MainForm extends JFrame {
    private int activeChar = 0;
    private final Set<Integer> selectedChars = new HashSet<>();
    private final Set<Integer> foundChars = new HashSet<>();
    private StyleManager styleManager;

    public MainForm() {
        String[] data = new String[2];
        new InputForm(data);
        String pattern = data[0];
        String text = data[1];

        setTitle("Knuth-Morris-Pratt Automaton");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Automaton automaton = new Automaton(pattern);

        AutomatonGraph automatonGraph = new AutomatonGraph(automaton);
        Viewer viewer = new SwingViewer(automatonGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = (ViewPanel) viewer.addDefaultView(false);
        add(view, BorderLayout.CENTER);

        JTextPane textPane = setupTextPane(text);

        Searcher searcher = new Searcher(
                automaton,
                text,
                this::handleFound,
                this::handleStateChange,
                this::handleStep);

        JButton nextStepButton = new JButton("Next Step");
        nextStepButton.addActionListener(e -> {
            nextStepButton.setEnabled(searcher.nextStep());
            automatonGraph.changeActiveNode(searcher.getAutomatonState());
        });

        setupControlPanel(textPane, nextStepButton);

        setVisible(true);
    }

    private void handleFound(int start, int end) {
        for (int i = start; i < end; i++) {
            foundChars.add(i);
        }
        styleManager.setFound(start, end);
        //System.out.println("\n\nonFound:");
        //System.out.println(a + " " + b);
    }

    private void handleStateChange(int start, int end) {
        for (int i : new HashSet<>(selectedChars)) {
            selectedChars.remove(i);
            restoreColor(i);
        }
        for (int i = start; i < end; i++) {
            selectedChars.add(i);
        }
        styleManager.setSelected(start, end);
        //System.out.println("\n\nonStateChange:");
        //System.out.println(a + " " + b);
    }

    private void handleStep(int index) {
        restoreColor(activeChar);
        activeChar = index;
        styleManager.setActive(activeChar, activeChar + 1);
        //System.out.println("\n\nonStep:");
        //System.out.println(a);
    }

    private void restoreColor(int index) {
        if (foundChars.contains(index)) {
            styleManager.setFound(index, index + 1);
        } else if (selectedChars.contains(index)) {
            styleManager.setSelected(index, index + 1);
        } else {
            styleManager.setDefault(index, index + 1);
        }
    }

    private JTextPane setupTextPane(String text) {
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        styleManager = new StyleManager(textPane.getStyledDocument());
        textPane.setEditable(false);
        textPane.setVisible(true);
        textPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        textPane.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        return textPane;
    }

    private void setupControlPanel(JTextPane textPane, JButton nextStepButton) {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(textPane, BorderLayout.CENTER);
        controlPanel.add(nextStepButton, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);
    }
}
