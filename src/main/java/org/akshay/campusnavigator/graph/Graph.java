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

    public void addEdge(Long source, Long destination, Double distance, boolean directed) {
        int sourceNodeId = Integer.parseInt(source.toString());
        int destinationNodeId = Integer.parseInt(destination.toString());
        AdjacencyListNode temp = adjacencyList.get(sourceNodeId);
        AdjacencyListNode node = new AdjacencyListNode(source, destination, distance, temp);
        adjacencyList.set(sourceNodeId, node);

        if (!directed) {
            temp = adjacencyList.get(destinationNodeId);
            node = new AdjacencyListNode(destination, source, distance, temp);
            adjacencyList.set(destinationNodeId, node);
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
