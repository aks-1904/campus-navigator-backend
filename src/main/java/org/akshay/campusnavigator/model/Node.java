package org.akshay.campusnavigator.model;

import jakarta.persistence.*;
import org.akshay.campusnavigator.enums.NodeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nodes", indexes = {
        @Index(name = "idx_node_type", columnList = "type"),
        @Index(name = "idx_node_parent", columnList = "parent_node_id")
})
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo;

    @Column(name = "is_accessible")
    private Boolean isAccessible = false;
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    private NodeType type;

    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    private Integer floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_node_id")
    private Node parentNode;

    @OneToMany(mappedBy = "parentNode", cascade = CascadeType.ALL)
    private List<Node> childNodes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sourceNode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> outgoingEdges = new ArrayList<>();
    @OneToMany(mappedBy = "destinationNode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> incomingEdges = new ArrayList<>();

    public Node() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
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

    public Node(Long id,
                String name,
                String description,
                String extraInfo,
                Boolean isAccessible,
                Boolean active,
                NodeType type,
                Double latitude,
                Double longitude,
                Integer floor,
                Node parentNode,
                List<Node> childNodes,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                List<Edge> outgoingEdges,
                List<Edge> incomingEdges) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.extraInfo = extraInfo;
        this.isAccessible = isAccessible;
        this.active = active;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
        this.parentNode = parentNode;
        this.childNodes = childNodes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.outgoingEdges = outgoingEdges;
        this.incomingEdges = incomingEdges;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Boolean getAccessible() {
        return isAccessible;
    }

    public void setAccessible(Boolean accessible) {
        isAccessible = accessible;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void setOutgoingEdges(List<Edge> outgoingEdges) {
        this.outgoingEdges = outgoingEdges;
    }

    public List<Edge> getIncomingEdges() {
        return incomingEdges;
    }

    public void setIncomingEdges(List<Edge> incomingEdges) {
        this.incomingEdges = incomingEdges;
    }
}
