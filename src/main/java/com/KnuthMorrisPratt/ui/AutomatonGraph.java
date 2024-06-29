package com.KnuthMorrisPratt.ui;

import com.KnuthMorrisPratt.logic.Automaton;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static com.KnuthMorrisPratt.Constants.ALPHABET;

public class AutomatonGraph extends SingleGraph {
    private boolean up;
    private Node activeNode;
    private Node previousNode;
    private final Queue<Edge> activeEdge;

    public AutomatonGraph(Automaton automaton) {
        super("automaton");
        System.setProperty("org.graphstream.ui", "swing");

        HashMap<Integer, String>[] edges = getEdges(automaton);
        activeEdge = new LinkedList<>();

        for (int i = 0; i < edges.length; i++) {
            Node node = addNode(Integer.toString(i));
            node.setAttribute("xy", i, 0);
            node.setAttribute("ui.label", node.getId());
            node.setAttribute("ui.class", "default");
        }
        getNode(edges.length - 1).setAttribute("ui.class", "terminal");
        activeNode = getNode(0);
        activeNode.setAttribute("ui.class", "default,active");

        up = true;
        for (int i = 0; i < edges.length; i++) {
            for (Map.Entry<Integer, String> entry : edges[i].entrySet()) {
                addAutomatonEdge(i, entry.getKey(), entry.getValue());
            }
        }

        setAttribute("ui.stylesheet", "url('file:styles.css')");
        setAttribute("ui.quality");
        setAttribute("ui.antialias");
    }

    public void changeActiveNode(int newActiveNode) {
        while (!activeEdge.isEmpty()) {
            Edge edge = activeEdge.poll();
            String edgeClass = (String) edge.getAttribute("ui.class");
            edge.setAttribute("ui.class", edgeClass.substring(0, edgeClass.indexOf(",")));
        }

        if (activeNode.getIndex() <= newActiveNode) {
            activeEdge.add(getEdge(activeNode.getIndex() + "to" + newActiveNode));
        } else {
            String supplementaryNodeId = activeNode.getIndex() + "o" + newActiveNode;
            activeEdge.add(getEdge(activeNode.getIndex() + "to" + supplementaryNodeId));
            activeEdge.add(getEdge(supplementaryNodeId + "to" + newActiveNode));
        }

        for (Edge edge : activeEdge) {
            edge.setAttribute("ui.class", edge.getAttribute("ui.class") + ",active");
        }

        if (newActiveNode == activeNode.getIndex()) {
            if (previousNode != null) {
                String previousNodeClass = (String) previousNode.getAttribute("ui.class");
                previousNode.setAttribute("ui.class", previousNodeClass.substring(0, previousNodeClass.indexOf(",")));
                previousNode = null;
            }
            return;
        }

        String activeNodeClass = (String) activeNode.getAttribute("ui.class");
        activeNode.setAttribute("ui.class", activeNodeClass.substring(0, activeNodeClass.indexOf(",")));

        if (previousNode != null) {
            String previousNodeClass = (String) previousNode.getAttribute("ui.class");
            previousNode.setAttribute("ui.class", previousNodeClass.substring(0, previousNodeClass.indexOf(",")));
        }
        previousNode = activeNode;
        previousNode.setAttribute("ui.class", previousNode.getAttribute("ui.class") + ",previous");

        activeNode = getNode(newActiveNode);
        activeNode.setAttribute("ui.class", activeNode.getAttribute("ui.class") + ",active");
    }

    private void addAutomatonEdge(int node1, int node2, String label) {
        if (node1 <= node2) {
            Edge edge = addEdge(node1 + "to" + node2, node1, node2, true);
            edge.setAttribute("ui.class", node1 == node2 ? "loop" : "forward");
            edge.setAttribute("ui.label", label);
        } else {
            Node node = addNode(node1 + "o" + node2);
            node.setAttribute("ui.class", "supplementary");
            node.setAttribute("xy", (node1 + node2) / 2., (node1 - node2) / 4. * (up ? 1 : -1));
            node.setAttribute("ui.label", label);

            addEdge(node1 + "to" + node.getId(), getNode(node1), node)
                    .setAttribute("ui.class", "back1");
            addEdge(node.getId() + "to" + node2, node, getNode(node2), true)
                    .setAttribute("ui.class", "back2");

            up = !up;
        }
    }

    private HashMap<Integer, String>[] getEdges(Automaton automaton) {
        HashMap<Integer, String>[] edges = new HashMap[automaton.getPatternLength() + 1];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new HashMap<>();
            for (char ch : ALPHABET.toCharArray()) {
                if (edges[i].containsKey(automaton.getNextState(i, ch))) {
                    edges[i].put(automaton.getNextState(i, ch), edges[i].get(automaton.getNextState(i, ch)) + ", " + ch);
                } else {
                    edges[i].put(automaton.getNextState(i, ch), Character.toString(ch));
                }
            }
        }
        return edges;
    }
}
