package org.akshay.campusnavigator.dto;

import org.akshay.campusnavigator.enums.EdgeType;
import org.akshay.campusnavigator.enums.NodeType;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseDTOs {

    public static class NodeResponse {

        private Long id;
        private String name;
        private NodeType nodeType;
        private Double latitude;
        private Double longitude;
        private Integer floor;
        private Long parentNodeId; // null if top-level node
        private String parentNodeName;
        private String description;
        private String extraInfo;
        private boolean isAccessible;
        private boolean active;
        private List<NodeResponse> childNodes; // entrances etc.
        private LocalDateTime createdAt;

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNodeType(NodeType nodeType) {
            this.nodeType = nodeType;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public void setFloor(Integer floor) {
            this.floor = floor;
        }

        public void setParentNodeId(Long parentNodeId) {
            this.parentNodeId = parentNodeId;
        }

        public void setParentNodeName(String parentNodeName) {
            this.parentNodeName = parentNodeName;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setExtraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
        }

        public void setAccessible(boolean accessible) {
            isAccessible = accessible;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setChildNodes(List<NodeResponse> childNodes) {
            this.childNodes = childNodes;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public Long getId() {
            return id;
        }

        public NodeType getNodeType() {
            return nodeType;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public Integer getFloor() {
            return floor;
        }

        public Long getParentNodeId() {
            return parentNodeId;
        }

        public String getParentNodeName() {
            return parentNodeName;
        }

        public String getDescription() {
            return description;
        }

        public String getExtraInfo() {
            return extraInfo;
        }

        public boolean isAccessible() {
            return isAccessible;
        }

        public boolean isActive() {
            return active;
        }

        public List<NodeResponse> getChildNodes() {
            return childNodes;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    public static class EdgeResponse {
        private Long id;
        private Long sourceNodeId;
        private String sourceNodeName;
        private Long targetNodeId;
        private String targetNodeName;
        private Double distance; // In meters
        private EdgeType edgeType;
        private boolean isBidirectional;
        private boolean isAccessible;
        private boolean active;
        private int waypointCount;
        private List<PathWaypointDTO> waypoints;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getSourceNodeId() {
            return sourceNodeId;
        }

        public void setSourceNodeId(Long sourceNodeId) {
            this.sourceNodeId = sourceNodeId;
        }

        public String getSourceNodeName() {
            return sourceNodeName;
        }

        public void setSourceNodeName(String sourceNodeName) {
            this.sourceNodeName = sourceNodeName;
        }

        public Long getTargetNodeId() {
            return targetNodeId;
        }

        public void setTargetNodeId(Long targetNodeId) {
            this.targetNodeId = targetNodeId;
        }

        public String getTargetNodeName() {
            return targetNodeName;
        }

        public void setTargetNodeName(String targetNodeName) {
            this.targetNodeName = targetNodeName;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public EdgeType getEdgeType() {
            return edgeType;
        }

        public void setEdgeType(EdgeType edgeType) {
            this.edgeType = edgeType;
        }

        public boolean isBidirectional() {
            return isBidirectional;
        }

        public void setBidirectional(boolean bidirectional) {
            isBidirectional = bidirectional;
        }

        public boolean isAccessible() {
            return isAccessible;
        }

        public void setAccessible(boolean accessible) {
            isAccessible = accessible;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getWaypointCount() {
            return waypointCount;
        }

        public void setWaypointCount(int waypointCount) {
            this.waypointCount = waypointCount;
        }

        public List<PathWaypointDTO> getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(List<PathWaypointDTO> waypoints) {
            this.waypoints = waypoints;
        }
    }

    public static class GraphResponse{
        private List<NodeResponse> nodes;
        private List<EdgeResponse> edges;

        public void setNodes(List<NodeResponse> nodes) {
            this.nodes = nodes;
        }

        public void setEdges(List<EdgeResponse> edges) {
            this.edges = edges;
        }

        public List<NodeResponse> getNodes() {
            return nodes;
        }

        public List<EdgeResponse> getEdges() {
            return edges;
        }
    }

    public static class ApiResponse<T> {

        private boolean success;
        private String message;
        private T data;

        public ApiResponse() {
        }

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public ApiResponse(boolean b, String message) {
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public static <T> ApiResponse<T> ok(String message, T data) {
            return new ApiResponse<>(true, message, data);
        }

        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, message);
        }

    }

}
