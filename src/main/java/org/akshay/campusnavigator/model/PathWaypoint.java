package org.akshay.campusnavigator.model;

import jakarta.persistence.*;

@Entity
@Table(name = "path_waypoints", indexes = {
        @Index(name = "idx_waypoint_edge", columnList = "edge_id"),
        @Index(name = "idx_waypoint_seq", columnList = "edge_id, sequence_order")
})
public class PathWaypoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edge_id", nullable = false)
    private Edge edge;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    private Double altitude; // Optional

    public PathWaypoint(Long id,
                        Edge edge,
                        Integer sequenceOrder,
                        Double latitude,
                        Double longitude,
                        Double altitude) {
        this.id = id;
        this.edge = edge;
        this.sequenceOrder = sequenceOrder;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public PathWaypoint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
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

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
}
