package org.akshay.campusnavigator.controller;

import org.akshay.campusnavigator.dto.NodeRequestDTO;
import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.model.Node;
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

    public AdminController(NodeService nodeService) {
        this.nodeService = nodeService;
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

}
