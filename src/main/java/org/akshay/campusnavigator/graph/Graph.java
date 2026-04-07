package org.akshay.campusnavigator.graph;

import java.util.ArrayList;

public class Graph {
    int vertices;
    ArrayList<AdjacencyListNode> adjacencyList;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>(vertices);

        for (int i = 0; i < vertices; ++i) {
            this.adjacencyList.set(i, null);
        }
    }

    public void addEdge(int source, int destination, int distance, boolean directed) {
        AdjacencyListNode temp = adjacencyList.get(source);
        AdjacencyListNode node = new AdjacencyListNode(source, destination, distance, temp);
        adjacencyList.set(source, node);

        if (!directed) {
            temp = adjacencyList.get(destination);
            node = new AdjacencyListNode(destination, source, distance, temp);
            adjacencyList.set(destination, node);
        }

    }

    public AdjacencyListNode getNeighbours(int source)
    {
        return adjacencyList.get(source);
    }

    public ArrayList<AdjacencyListNode> getGraphAsAdjacencyList() {
        return this.adjacencyList;
    }
}
