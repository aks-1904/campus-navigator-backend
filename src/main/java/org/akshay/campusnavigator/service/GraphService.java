package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.graph.Algorithm;
import org.akshay.campusnavigator.graph.Graph;
import org.akshay.campusnavigator.model.Edge;
import org.akshay.campusnavigator.repository.EdgeRepository;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GraphService {

    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;
    private final NodeService nodeService;
    private final EdgeService edgeService;

    public GraphService(NodeRepository nodeRepository, EdgeRepository edgeRepository, NodeService nodeService, EdgeService edgeService) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
        this.nodeService = nodeService;
        this.edgeService = edgeService;
    }

    @Transactional(readOnly = true)
    public ResponseDTOs.GraphResponse getGraph() {
        List<ResponseDTOs.NodeResponse> nodes = nodeRepository
                .findAll()
                .stream()
                .map(n -> nodeService.toResponse(n, false))
                .collect(Collectors.toList());

        List<ResponseDTOs.EdgeResponse> edges = edgeRepository
                .findAll()
                .stream()
                .map(e -> edgeService.toResponse(e, false))
                .collect(Collectors.toList());

        ResponseDTOs.GraphResponse graph = new ResponseDTOs.GraphResponse();
        graph.setEdges(edges);
        graph.setNodes(nodes);

        return graph;
    }

    @Transactional(readOnly = true)
    public List<ResponseDTOs.EdgeResponse> getShortestPath(Long sourceNodeId, Long destinationNodeId) {

        List<Edge> edges = edgeRepository.findAll();
        Graph graph = createGraph(edges);

        List<Integer> nodePath =
                Algorithm.Dijkstra.shortestPath(graph, sourceNodeId.intValue(), destinationNodeId.intValue());

        if (nodePath.size() < 2)
            return new ArrayList<>();

        List<Edge> edgesData = new ArrayList<>();

        for (int i = 0; i < nodePath.size() - 1; i++) {

            long u = nodePath.get(i);
            long v = nodePath.get(i + 1);

            Edge edge = edgeRepository
                    .findBySourceNodeIdAndDestinationNodeId(u, v)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Edge not found between " + u + " and " + v));

            edgesData.add(edge);
        }

        return edgesData
                .stream()
                .map(e -> edgeService.toResponse(e, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseDTOs.EdgeResponse> getAllShortestPath(Long sourceNodeId, Long destinationNodeId) {

        List<Edge> edges = edgeRepository.findAll();
        Graph graph = createGraph(edges);

        // Run Floyd Warshall
        Map<Integer, Map<Integer, List<Integer>>> allPaths =
                Algorithm.FloydWarshall.shortestPaths(graph);

        List<Integer> nodePath = allPaths
                .getOrDefault(sourceNodeId.intValue(), new HashMap<>())
                .get(destinationNodeId.intValue());

        if (nodePath == null || nodePath.size() < 2)
            return new ArrayList<>();

        List<Edge> edgesData = new ArrayList<>();

        for (int i = 0; i < nodePath.size() - 1; i++) {

            int u = nodePath.get(i);
            int v = nodePath.get(i + 1);

            Edge edge = edgeRepository
                    .findBySourceNodeIdAndDestinationNodeId((long) u, (long) v)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Edge not found between " + u + " and " + v));

            edgesData.add(edge);
        }

        return edgesData
                .stream()
                .map(e -> edgeService.toResponse(e, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private Graph createGraph(List<Edge> edges) {
        Graph graph = new Graph(edges.size() + 1);
        for (Edge e : edges) {
            graph.addEdge(e.getSourceNode().getId(),
                    e.getDestinationNode().getId(),
                    e.getDistance(),
                    e.getBidirectional());
        }

        return graph;
    }

}
