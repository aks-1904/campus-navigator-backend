package org.akshay.campusnavigator.graph;

public class AdjacencyListNode {
    public Long sourceNode;
    public Long destinationNode;
    public Double distance;
    AdjacencyListNode next;

    public AdjacencyListNode(Long sourceNode, Long destinationNode, Double distance, AdjacencyListNode next) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distance = distance;
        this.next = next;
    }

    public AdjacencyListNode(Long sourceNode, Long destinationNode, Double distance) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distance = distance;
        this.next = null;
    }

    public AdjacencyListNode(Long sourceNode, Long destinationNode) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.distance = 0.0; // For nodes inside building
        this.next = null;
    }

}
