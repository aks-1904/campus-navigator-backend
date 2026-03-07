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

import java.util.ArrayList;
import java.util.List;

@Service
public class EdgeService {
    private static final Logger log = LoggerFactory.getLogger(EdgeService.class);
    NodeRepository nodeRepository;
    EdgeRepository edgeRepository;

    public EdgeService(NodeRepository nodeRepository, EdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

    public EdgeResponse createEdge(EdgeRequestDTO request) {

        Node source = nodeRepository
                .findById(request.getSourceNodeId())
                .orElseThrow(() -> new IllegalArgumentException("Source node not found: " + request.getSourceNodeId()));
        Node destination = nodeRepository
                .findById(request.getDestinationNodeId())
                .orElseThrow(() -> new IllegalArgumentException("Destination node not found: " + request.getDestinationNodeId()));

        List<PathWaypointDTO> coords = request.getWaypoints();
        double[] lats = coords.stream().mapToDouble(PathWaypointDTO::getLatitude).toArray();
        double[] longs = coords.stream().mapToDouble(PathWaypointDTO::getLongitude).toArray();
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
        for (int i = 1; i < coords.size(); ++i) {
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

    private EdgeResponse toResponse(Edge edge, boolean includeWaypoints) {

        List<PathWaypointDTO> waypoints = null;
        if (includeWaypoints) {
            waypoints = edge.getWaypoints().stream()
                    .map(wp -> {
                        PathWaypointDTO p = new PathWaypointDTO();
                        p.setLatitude(wp.getLatitude());
                        p.setLatitude(wp.getLongitude());
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
}
