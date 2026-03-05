package org.akshay.campusnavigator.dto;

import org.akshay.campusnavigator.enums.EdgeType;
import org.akshay.campusnavigator.enums.NodeType;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ResponseDTOs {

    private class CoordinateDTO {

        private final Double latitude;
        private final Double longitude;
        private final Double altitude;

        public CoordinateDTO(Double latitude, Double longitude, Double altitude) {
            this.latitude = Objects.requireNonNull(latitude, "Latitude cannot be null");
            this.longitude = Objects.requireNonNull(longitude, "Longitude cannot be null");
            this.altitude = altitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public Double getAltitude() {
            return altitude;
        }
    }

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
        private List<CoordinateDTO> waypoints;
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
