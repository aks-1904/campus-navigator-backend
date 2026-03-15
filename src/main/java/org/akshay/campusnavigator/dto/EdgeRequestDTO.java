package org.akshay.campusnavigator.dto;

import org.akshay.campusnavigator.enums.EdgeType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class EdgeRequestDTO {

    @NotNull
    private Long sourceNodeId;
    @NotNull
    private Long destinationNodeId;

    @NotNull
    private EdgeType edgeType = EdgeType.WALKWAY;

    private Boolean isAccessible = true;
    private Boolean isBidirectional = true;
    private Boolean active = true;

    private String description;

    private List<PathWaypointDTO> waypoints;

    public Boolean getBidirectional() {
        return isBidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        isBidirectional = bidirectional;
    }

    public Long getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(Long sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public Long getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(Long destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
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

    public List<PathWaypointDTO> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<PathWaypointDTO> waypoints) {
        this.waypoints = waypoints;
    }
}
