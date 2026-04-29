package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.dto.EdgeRequestDTO;
import org.akshay.campusnavigator.dto.PathWaypointDTO;
import org.akshay.campusnavigator.dto.ResponseDTOs.EdgeResponse;
import org.akshay.campusnavigator.model.Edge;
import org.akshay.campusnavigator.model.Node;
import org.akshay.campusnavigator.model.PathWaypoint;
import org.akshay.campusnavigator.repository.EdgeRepository;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.akshay.campusnavigator.util.Haversin;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EdgeService {
    private static final Logger log = LoggerFactory.getLogger(EdgeService.class);
    NodeRepository nodeRepository;
    EdgeRepository edgeRepository;

    public EdgeService(NodeRepository nodeRepository, EdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

    @Transactional()
    public EdgeResponse createEdge(EdgeRequestDTO request) {

        Node source = nodeRepository
                .findById(request.getSourceNodeId())
                .orElseThrow(() -> new IllegalArgumentException("Source node not found: " + request.getSourceNodeId()));
        Node destination = nodeRepository
                .findById(request.getDestinationNodeId())
                .orElseThrow(() -> new IllegalArgumentException("Destination node not found: " + request.getDestinationNodeId()));

        List<PathWaypointDTO> coords = request.getWaypoints();

        List<Double> latList = new ArrayList<>();
        List<Double> lonList = new ArrayList<>();

        latList.add(source.getLatitude());
        lonList.add(source.getLongitude());

        for (PathWaypointDTO p : coords) {
            latList.add(p.getLatitude());
            lonList.add(p.getLongitude());
        }

        latList.add(destination.getLatitude());
        lonList.add(destination.getLongitude());

        double[] lats = latList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] longs = lonList.stream().mapToDouble(Double::doubleValue).toArray();
        double distanceMeters = Haversin.totalPathDistance(lats, longs);

        log.info("Recording path: {} -> {} | {} coords | {:.2f}m", source.getName(), destination.getName(), coords.size(), distanceMeters);

        Edge edge = new Edge();
        edge.setSourceNode(source);
        edge.setDestinationNode(destination);
        edge.setDistance(distanceMeters);
        edge.setEdgeType(request.getEdgeType());
        edge.setBidirectional(request.getBidirectional());
        edge.setAccessible(request.getAccessible());
        edge.setDescription(request.getDescription());

        List<PathWaypoint> waypoints = new ArrayList<>();
        for (int i = 0; i < coords.size(); ++i) {
            PathWaypointDTO p = coords.get(i);
            PathWaypoint wp = new PathWaypoint();
            wp.setEdge(edge);
            wp.setSequenceOrder(i);
            wp.setLatitude(p.getLatitude());
            wp.setLongitude(p.getLongitude());
            wp.setAltitude(p.getAltitude());

            waypoints.add(wp);
        }
        edge.setWaypoints(waypoints);

        edge = edgeRepository.save(edge);
        log.info("Saved PathEdge id={} with {} waypoints", edge.getId(), waypoints.size());

        return toResponse(edge, true);

    }

    public EdgeResponse getEdgeById(Long id) {
        Edge edge = edgeRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge not found " + id));

        return toResponse(edge, true);
    }

    @Transactional
    public EdgeResponse updateEdge(Long id, EdgeRequestDTO edgeDTO) {
        Edge edge = edgeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge not found with ID: " + id));

        // 1. Update Source and Destination Nodes if provided
        if (edgeDTO.getSourceNodeId() != null && !edgeDTO.getSourceNodeId().equals(edge.getSourceNode().getId())) {
            Node sourceNode = nodeRepository.findById(edgeDTO.getSourceNodeId())
                    .orElseThrow(() -> new IllegalArgumentException("Source Node not found"));
            edge.setSourceNode(sourceNode);
        }

        if (edgeDTO.getDestinationNodeId() != null && !edgeDTO.getDestinationNodeId().equals(edge.getDestinationNode().getId())) {
            Node destNode = nodeRepository.findById(edgeDTO.getDestinationNodeId())
                    .orElseThrow(() -> new IllegalArgumentException("Destination Node not found"));
            edge.setDestinationNode(destNode);
        }

        // 2. Update simple primitive fields
        if (edgeDTO.getBidirectional() != null) edge.setBidirectional(edgeDTO.getBidirectional());
        if (edgeDTO.getAccessible() != null) edge.setAccessible(edgeDTO.getAccessible());
        if (edgeDTO.getActive() != null) edge.setActive(edgeDTO.getActive());
        if (edgeDTO.getEdgeType() != null) edge.setEdgeType(edgeDTO.getEdgeType());
        if (edgeDTO.getDescription() != null) edge.setDescription(edgeDTO.getDescription());

        // 3. Update Waypoints (if provided)
        if (edgeDTO.getWaypoints() != null) {
            edge.getWaypoints().clear(); // Remove old waypoints

            for (int i = 0; i < edgeDTO.getWaypoints().size(); ++i) {
                PathWaypointDTO wpDto = edgeDTO.getWaypoints().get(i);
                PathWaypoint wp = new PathWaypoint();
                wp.setEdge(edge);
                wp.setSequenceOrder(i);
                wp.setLatitude(wpDto.getLatitude());
                wp.setLongitude(wpDto.getLongitude());
                wp.setAltitude(wpDto.getAltitude());
                edge.getWaypoints().add(wp);
            }
        }

        // 4. Recalculate Distance Dynamically
        Node currentSource = edge.getSourceNode();
        Node currentDest = edge.getDestinationNode();

        List<Double> latList = new ArrayList<>();
        List<Double> lonList = new ArrayList<>();

        // Add source coordinates
        latList.add(currentSource.getLatitude());
        lonList.add(currentSource.getLongitude());

        // Add waypoint coordinates
        if (edgeDTO.getWaypoints() != null) {
            // If new waypoints were provided in the DTO, use them
            for (PathWaypointDTO p : edgeDTO.getWaypoints()) {
                latList.add(p.getLatitude());
                lonList.add(p.getLongitude());
            }
        } else {
            // If waypoints weren't updated, use existing ones (ensure they are ordered)
            List<PathWaypoint> currentWaypoints = new ArrayList<>(edge.getWaypoints());
            currentWaypoints.sort(Comparator.comparingInt(PathWaypoint::getSequenceOrder));
            for (PathWaypoint p : currentWaypoints) {
                latList.add(p.getLatitude());
                lonList.add(p.getLongitude());
            }
        }

        // Add destination coordinates
        latList.add(currentDest.getLatitude());
        lonList.add(currentDest.getLongitude());

        // Calculate distance via Haversin
        double[] lats = latList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] longs = lonList.stream().mapToDouble(Double::doubleValue).toArray();
        double distanceMeters = Haversin.totalPathDistance(lats, longs);

        edge.setDistance(distanceMeters);

        // 5. Save and return
        Edge updatedEdge = edgeRepository.save(edge);

        // Optional: log.info("Updated PathEdge id={} with recalculated distance {:.2f}m", updatedEdge.getId(), distanceMeters);

        return toResponse(updatedEdge, true);
    }

    @Transactional(readOnly = true)
    public EdgeResponse toResponse(Edge edge, boolean includeWaypoints) {

        List<PathWaypointDTO> waypoints = null;
        if (includeWaypoints) {
            waypoints = edge.getWaypoints().stream()
                    .map(wp -> {
                        PathWaypointDTO p = new PathWaypointDTO();
                        p.setLatitude(wp.getLatitude());
                        p.setLongitude(wp.getLongitude());
                        p.setAltitude(wp.getAltitude());

                        return p;
                    })
                    .toList();
        }

        return getEdgeResponse(edge, waypoints);

    }

    private static @NonNull EdgeResponse getEdgeResponse(Edge edge, List<PathWaypointDTO> waypoints) {
        EdgeResponse edgeResponse = new EdgeResponse();
        edgeResponse.setId(edge.getId());
        edgeResponse.setSourceNodeId(edge.getSourceNode().getId());
        edgeResponse.setTargetNodeId(edge.getDestinationNode().getId());
        edgeResponse.setSourceNodeName(edge.getSourceNode().getName());
        edgeResponse.setTargetNodeName(edge.getDestinationNode().getName());
        edgeResponse.setDistance(edge.getDistance());
        edgeResponse.setEdgeType(edge.getEdgeType());
        edgeResponse.setBidirectional(edge.getBidirectional());
        edgeResponse.setAccessible(edge.getAccessible());
        edgeResponse.setActive(edge.getActive());
        edgeResponse.setWaypointCount(edge.getWaypoints().size());
        edgeResponse.setWaypoints(waypoints);
        return edgeResponse;
    }

    public List<EdgeResponse> getAllEdges() {
        List<Edge> edges = edgeRepository.findAll();
        List<EdgeResponse> edgesData;

        edgesData = edges
                .stream()
                .map(e -> toResponse(e, true))
                .collect(Collectors.toList());

        return edgesData;
    }
}
