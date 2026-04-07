package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.dto.ResponseDTOs.EdgeResponse;
import org.akshay.campusnavigator.dto.ResponseDTOs.GraphResponse;
import org.akshay.campusnavigator.repository.EdgeRepository;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    NodeRepository nodeRepository;
    EdgeRepository edgeRepository;

    public GraphService(NodeRepository nodeRepository, EdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

    public GraphResponse getGraph() {
        return null; //to be implemented later.
    }

    public List<EdgeResponse> getShortestPath(Long sourceNodeId, Long destinationNodeId) {
        return null; //to be implemented later.
    }
}
