package org.akshay.campusnavigator.graph;

import java.util.ArrayList;

public class Graph {
    int vertices;
    ArrayList<AdjacencyListNode> adjacencyList;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>(vertices);

        for(int i = 0; i < vertices; ++i) {
            this.adjacencyList.set(i, null);
        }
    }

    public ArrayList<AdjacencyListNode> getGraphAsAdjacencyList() {
        return this.adjacencyList;
    }
}
