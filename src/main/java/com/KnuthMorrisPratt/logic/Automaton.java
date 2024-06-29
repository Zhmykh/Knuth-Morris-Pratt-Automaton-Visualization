package com.KnuthMorrisPratt.logic;

import static com.KnuthMorrisPratt.Constants.ALPHABET;

public class Automaton {
    private final int[][] deltaFunction;

    public Automaton(String pattern) {
        int[] pi = prefixFunction(pattern);
        deltaFunction = new int[pattern.length() + 1][ALPHABET.length()];
        for (int i = 0; i < pattern.length() + 1; i++) {
            for (int j = 0; j < ALPHABET.length(); j++) {
                if (i < pattern.length() && pattern.charAt(i) == ALPHABET.charAt(j)) {
                    deltaFunction[i][j] = i + 1;
                } else if (i > 0) {
                    deltaFunction[i][j] = deltaFunction[pi[i - 1]][j];
                } else {
                    deltaFunction[i][j] = 0;
                }
            }
        }
    }

    public int getNextState(int currentState, char ch) {
        int i = ALPHABET.indexOf(ch);
        if (i == -1) {
            return 0;
        }
        return deltaFunction[currentState][i];
    }

    public int getPatternLength() {
        return deltaFunction.length - 1;
    }

    private int[] prefixFunction(String string) {
        int[] result = new int[string.length()];
        for (int i = 1; i < string.length(); i++) {
            int k = result[i - 1];
            while (k > 0 && string.charAt(k) != string.charAt(i)) {
                k = result[k - 1];
            }
            if (string.charAt(k) == string.charAt(i)) {
                k++;
            }
            result[i] = k;
        }
        return result;
    }
}
