package org.akshay.campusnavigator.controller;

import org.akshay.campusnavigator.dto.EdgeRequestDTO;
import org.akshay.campusnavigator.dto.NodeRequestDTO;
import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.service.EdgeService;
import org.akshay.campusnavigator.service.NodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final NodeService nodeService;
    private final EdgeService edgeService;

    public AdminController(NodeService nodeService, EdgeService edgeService) {
        this.nodeService = nodeService;
        this.edgeService = edgeService;
    }

    @PostMapping("/node")
    public ResponseEntity<
            ResponseDTOs.ApiResponse<
                    ResponseDTOs.NodeResponse
                    >
            > createNode(@RequestBody NodeRequestDTO node) {
        return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        "Node created successfully", nodeService.createNode(node)
                )
        );
    }

    @PostMapping("/edge")
    public ResponseEntity<
            ResponseDTOs.ApiResponse<
                    ResponseDTOs.EdgeResponse
                    >
            > createEdge(@RequestBody EdgeRequestDTO edge) {
        return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        "Edge created successfully", edgeService.createEdge(edge)
                )
        );
    }

}
