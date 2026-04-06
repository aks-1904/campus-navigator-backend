package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.repository.EdgeRepository;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.springframework.stereotype.Service;

@Service
public class GraphService {

    NodeRepository nodeRepository;
    EdgeRepository edgeRepository;

    public GraphService(NodeRepository nodeRepository, EdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

}
