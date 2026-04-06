package org.akshay.campusnavigator.graph;

public class AdjacencyListNode {
    public int sourceNode;
    public int destinationNode;
    public int distance;
    AdjacencyListNode next;

    public AdjacencyListNode(int sourceNode, int destinationNode, int distance, AdjacencyListNode next) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distance = distance;
        this.next = next;
    }

    public AdjacencyListNode(int sourceNode, int destinationNode, int distance) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distance = distance;
        this.next = null;
    }

    public AdjacencyListNode(int sourceNode, int destinationNode) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distance = 0; // For nodes inside building
        this.next = null;
    }

}
