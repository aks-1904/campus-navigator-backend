package org.akshay.campusnavigator.model;

import jakarta.persistence.*;
import org.akshay.campusnavigator.enums.EdgeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "edges", indexes = {
        @Index(name = "idx_edge_source", columnList = "source_node_id"),
        @Index(name = "idx_edge_destination", columnList = "target_node_id"),
})
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_node_id", nullable = false)
    private Node sourceNode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_node_id", nullable = false)
    private Node destinationNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "edge_type", nullable = false)
    private EdgeType edgeType = EdgeType.WALKWAY;

    @Column(nullable = false)
    private Double distance;

    @Column(name = "is_accessible")
    private Boolean isAccessible = true;
    @Column(name = "is_bidirectional")
    private Boolean isBidirectional = true;

    private Boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OrderBy("sequenceOrder ASC")
    @OneToMany(mappedBy = "edge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathWaypoint> waypoints = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(Node destinationNode) {
        this.destinationNode = destinationNode;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getAccessible() {
        return isAccessible;
    }

    public void setAccessible(Boolean accessible) {
        isAccessible = accessible;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<PathWaypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<PathWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public Boolean getBidirectional() {
        return isBidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        isBidirectional = bidirectional;
    }

    public Edge(Long id,
                Node sourceNode,
                Node destinationNode,
                EdgeType edgeType,
                Double distance,
                Boolean isAccessible,
                Boolean active,
                String description,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                List<PathWaypoint> waypoints) {
        this.id = id;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.edgeType = edgeType;
        this.distance = distance;
        this.isAccessible = isAccessible;
        this.active = active;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.waypoints = waypoints;
    }

    public Edge() {
    }
}
