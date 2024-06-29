package com.KnuthMorrisPratt.logic;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class Searcher {
    private final Automaton automaton;
    private final String text;
    private final BiConsumer<Integer, Integer> onFound;
    private final BiConsumer<Integer, Integer> onStateChange;
    private final IntConsumer onStep;
    private int currentPosition = -1;
    private int automatonState;

    public Searcher(Automaton automaton,
                    String text,
                    BiConsumer<Integer, Integer> onFound,
                    BiConsumer<Integer, Integer> onStateChange,
                    IntConsumer onStep) {
        this.automaton = automaton;
        this.text = text;
        this.onFound = onFound;
        this.onStateChange = onStateChange;
        this.onStep = onStep;
    }

    public boolean nextStep() {
        currentPosition++;

        automatonState = automaton.getNextState(automatonState, text.charAt(currentPosition));

        onStateChange.accept(currentPosition - automatonState + 1, currentPosition + 1);
        if (automatonState == automaton.getPatternLength()) {
            onFound.accept(currentPosition - automaton.getPatternLength() + 1, currentPosition + 1);
        }
        onStep.accept(currentPosition);

        return currentPosition < text.length() - 1;
    }

    public int getAutomatonState() {
        return automatonState;
    }
}
