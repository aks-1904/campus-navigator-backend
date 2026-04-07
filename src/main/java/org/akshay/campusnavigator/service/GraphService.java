package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.graph.Graph;
import org.akshay.campusnavigator.model.Edge;
import org.akshay.campusnavigator.repository.EdgeRepository;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return null; //to be implemented later.
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

    @Transactional(readOnly = true)
    public List<ResponseDTOs.EdgeResponse> getAllShortestPath(Long sourceNodeId, Long destinationNodeId) {
        return null; //to be implemented later.
    }
}
