package org.akshay.campusnavigator.service;

import org.akshay.campusnavigator.dto.NodeRequestDTO;
import org.akshay.campusnavigator.dto.ResponseDTOs.NodeResponse;
import org.akshay.campusnavigator.enums.NodeType;
import org.akshay.campusnavigator.model.Node;
import org.akshay.campusnavigator.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NodeService {

    private static final Logger log = LoggerFactory.getLogger(NodeService.class);
    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public NodeResponse createNode(NodeRequestDTO request) {
        Node parentNode = null;

        if (request.getParentNodeId() != null) {
            parentNode = nodeRepository.findById(request.getParentNodeId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent node not found: " + request.getParentNodeId()));

            if (request.getNodeType() == NodeType.ENTRANCE) {
                log.info("Creating ENTRANCE for building node id={}, name={}", parentNode.getId(), parentNode.getName());
            }
        }

        Node node = new Node();
        node.setName(request.getName());
        node.setType(request.getNodeType());
        node.setLatitude(request.getLatitude());
        node.setLongitude(request.getLongitude());
        node.setFloor(request.getFloor());
        node.setParentNode(parentNode);
        node.setDescription(request.getDescription());
        node.setExtraInfo(request.getExtraInfo());
        node.setAccessible(request.getAccessible() != null ? request.getAccessible() : false);

        node = nodeRepository.save(node);
        log.info("Created node id={} type={} name={}", node.getId(), node.getType(), node.getName());

        return toResponse(node, true);
    }

    private NodeResponse toResponse(Node node, boolean includeChildren) {

        List<NodeResponse> children = null;
        if(includeChildren && !node.getChildNodes().isEmpty()) {
            children = node
                    .getChildNodes()
                    .stream()
                    .map(c -> toResponse(c, false))
                    .collect(Collectors.toList());
        }

        NodeResponse response = new NodeResponse();
        response.setId(node.getId());
        response.setName(node.getName());
        response.setNodeType(node.getType());
        response.setLatitude(node.getLatitude());
        response.setLongitude(node.getLongitude());
        response.setFloor(node.getFloor());
        response.setParentNodeId(node.getParentNode() != null ? node.getParentNode().getId() : null);
        response.setParentNodeName(node.getParentNode() != null ? node.getParentNode().getName() : null);
        response.setDescription(node.getDescription());
        response.setExtraInfo(node.getExtraInfo());
        response.setAccessible(node.getAccessible());
        response.setActive(node.getActive());
        response.setChildNodes(children);
        response.setCreatedAt(node.getCreatedAt());

        return response;

    }

}
