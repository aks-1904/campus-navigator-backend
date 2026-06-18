package org.akshay.campusnavigator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.enums.NodeType;
import org.akshay.campusnavigator.graph.Algorithm;
import org.akshay.campusnavigator.graph.Graph;
import org.akshay.campusnavigator.model.Edge;
import org.akshay.campusnavigator.model.Node;
import org.akshay.campusnavigator.repository.EdgeRepository;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.enums.NodeType;
import org.akshay.campusnavigator.graph.Algorithm;
import org.akshay.campusnavigator.graph.Graph;
import org.akshay.campusnavigator.model.Edge;
import org.akshay.campusnavigator.model.Node;
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
                .map(e -> edgeService.toResponse(e, true))
                .collect(Collectors.toList());

        ResponseDTOs.GraphResponse graph = new ResponseDTOs.GraphResponse();
        graph.setEdges(edges);
        graph.setNodes(nodes);

        return graph;
    }

    @Transactional(readOnly = true)
    public List<ResponseDTOs.EdgeResponse> getShortestPath(Long sourceNodeId, Long destinationNodeId) {
        Node sourceNode = nodeRepository.findById(sourceNodeId).orElseThrow(() -> new IllegalArgumentException("Invalid Source"));
        Node destNode = nodeRepository.findById(destinationNodeId).orElseThrow(() -> new IllegalArgumentException("Invalid Dest"));

        List<Edge> edges = edgeRepository.findAll();
        List<Node> nodes = nodeRepository.findAll();
        int maxId = nodes.stream().mapToInt(n -> n.getId().intValue()).max().orElse(0);

        boolean sIndoor = sourceNode.getParentNode() != null;
        boolean sIsBuilding = sourceNode.getType() == NodeType.BUILDING;

        boolean dIndoor = destNode.getParentNode() != null;
        boolean dIsBuilding = destNode.getType() == NodeType.BUILDING;

        boolean sameBuilding = sIndoor && dIndoor && sourceNode.getParentNode().getId().equals(destNode.getParentNode().getId());

        List<Integer> finalNodePath;

        if (sameBuilding) {
            // Both inside the same building - strict BFS
            Graph indoorGraph = createIndoorGraph(edges, sourceNode.getParentNode().getId(), maxId);
            finalNodePath = Algorithm.BFS.shortestPath(indoorGraph, sourceNodeId.intValue(), destinationNodeId.intValue());
        } else {
            // Hierarchical Route: S -> S_Entrances -> D_Entrances -> D
            List<Node> sEntrances = getEntrances(nodes, sourceNode, sIndoor, sIsBuilding);
            List<Node> dEntrances = getEntrances(nodes, destNode, dIndoor, dIsBuilding);

            Graph outdoorGraph = createOutdoorGraph(edges, maxId);
            double minCost = Double.MAX_VALUE;
            List<Integer> bestPath = new ArrayList<>();

            for (Node sEnt : sEntrances) {
                for (Node dEnt : dEntrances) {
                    List<Integer> sPath = new ArrayList<>();
                    if (sIndoor) {
                        Graph sIndoorGraph = createIndoorGraph(edges, sourceNode.getParentNode().getId(), maxId);
                        sPath = Algorithm.BFS.shortestPath(sIndoorGraph, sourceNodeId.intValue(), sEnt.getId().intValue());
                    } else {
                        sPath.add(sEnt.getId().intValue()); // Was outdoor or entrance itself
                    }

                    // Outdoor Dijkstra
                    List<Integer> mPath = Algorithm.Dijkstra.shortestPath(outdoorGraph, sEnt.getId().intValue(), dEnt.getId().intValue());

                    List<Integer> dPath = new ArrayList<>();
                    if (dIndoor) {
                        Graph dIndoorGraph = createIndoorGraph(edges, destNode.getParentNode().getId(), maxId);
                        dPath = Algorithm.BFS.shortestPath(dIndoorGraph, dEnt.getId().intValue(), destinationNodeId.intValue());
                    } else {
                        dPath.add(dEnt.getId().intValue());
                    }

                    // Validate continuous path exists
                    if (sPath.isEmpty() || mPath.isEmpty() || dPath.isEmpty()) continue;

                    // Weighted combination: BFS hops count as 5 meters of "effort" roughly
                    double sCost = sPath.size() * 5.0;
                    double dCost = dPath.size() * 5.0;
                    double mCost = calculatePathDistance(mPath, edges);
                    double totalCost = sCost + mCost + dCost;

                    if (totalCost < minCost) {
                        minCost = totalCost;
                        bestPath.clear();
                        bestPath.addAll(sPath);
                        // Prevent duplicating the connection points
                        bestPath.addAll(mPath.subList(1, mPath.size()));
                        if (dIndoor) {
                            bestPath.addAll(dPath.subList(1, dPath.size()));
                        }
                    }
                }
            }
            finalNodePath = bestPath;
        }

        return buildEdgeResponses(finalNodePath, edges);
    }

    private List<Node> getEntrances(List<Node> nodes, Node node, boolean isIndoor, boolean isBuilding) {
        Long buildingId = isIndoor ? node.getParentNode().getId() : (isBuilding ? node.getId() : null);
        if (buildingId != null) {
            List<Node> entrances = nodes.stream()
                    .filter(n -> n.getType() == NodeType.ENTRANCE && n.getParentNode() != null && n.getParentNode().getId().equals(buildingId))
                    .collect(Collectors.toList());
            if (!entrances.isEmpty()) return entrances;
        }
        return List.of(node); // Fallback to itself if no entrances mapped
    }

    private Graph createIndoorGraph(List<Edge> edges, Long buildingId, int maxId) {
        Graph graph = new Graph(maxId + 1);
        for (Edge e : edges) {
            Node s = e.getSourceNode();
            Node d = e.getDestinationNode();

            boolean sIn = (s.getParentNode() != null && s.getParentNode().getId().equals(buildingId)) || (s.getType() == NodeType.ENTRANCE && s.getParentNode() != null && s.getParentNode().getId().equals(buildingId));
            boolean dIn = (d.getParentNode() != null && d.getParentNode().getId().equals(buildingId)) || (d.getType() == NodeType.ENTRANCE && d.getParentNode() != null && d.getParentNode().getId().equals(buildingId));

            if (sIn && dIn) {
                graph.addEdge(s.getId(), d.getId(), 1.0, e.getBidirectional()); // Weight 1 for BFS
            }
        }
        return graph;
    }

    private Graph createOutdoorGraph(List<Edge> edges, int maxId) {
        Graph graph = new Graph(maxId + 1);
        for (Edge e : edges) {
            Node s = e.getSourceNode();
            Node d = e.getDestinationNode();

            // Allow outdoor nodes and entrances, restrict pure indoor nodes
            boolean sOut = s.getParentNode() == null || s.getType() == NodeType.ENTRANCE;
            boolean dOut = d.getParentNode() == null || d.getType() == NodeType.ENTRANCE;

            if (sOut && dOut) {
                graph.addEdge(s.getId(), d.getId(), e.getDistance(), e.getBidirectional());
            }
        }
        return graph;
    }

    private double calculatePathDistance(List<Integer> path, List<Edge> edges) {
        double dist = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            long u = path.get(i);
            long v = path.get(i + 1);
            Edge edge = edges.stream()
                    .filter(e -> (e.getSourceNode().getId().equals(u) && e.getDestinationNode().getId().equals(v)) ||
                            (e.getBidirectional() && e.getSourceNode().getId().equals(v) && e.getDestinationNode().getId().equals(u)))
                    .findFirst().orElse(null);
            if (edge != null) dist += edge.getDistance();
            else dist += 10000; // Heavy penalty if edge missing logically
        }
        return dist;
    }

    private List<ResponseDTOs.EdgeResponse> buildEdgeResponses(List<Integer> nodePath, List<Edge> edges) {
        if (nodePath == null || nodePath.size() < 2) return new ArrayList<>();
        List<ResponseDTOs.EdgeResponse> responses = new ArrayList<>();

        for (int i = 0; i < nodePath.size() - 1; i++) {
            long u = nodePath.get(i);
            long v = nodePath.get(i + 1);

            Edge edge = edges.stream()
                    .filter(e -> (e.getSourceNode().getId().equals(u) && e.getDestinationNode().getId().equals(v)) ||
                            (e.getBidirectional() && e.getSourceNode().getId().equals(v) && e.getDestinationNode().getId().equals(u)))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Edge missing between " + u + " and " + v));

            responses.add(edgeService.toResponse(edge, true));
        }
        return responses;
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
                .map(e -> edgeService.toResponse(e, true))
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
