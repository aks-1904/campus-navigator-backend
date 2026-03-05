package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.repository.NodeRepository;
import org.springframework.stereotype.Service;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

}
