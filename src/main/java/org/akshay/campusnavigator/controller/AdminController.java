package org.akshay.campusnavigator.controller;

import org.akshay.campusnavigator.service.NodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final NodeService nodeService;

    public AdminController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

//    @PostMapping("/node")
//    public ResponseEntity<
//            ResponseDTOs.ApiResponse<
//                    ResponseDTOs.NodeResponse
//                    >
//            > createNode(@RequestBody Node node) {
//        return ResponseEntity.ok(
//                ResponseDTOs.ApiResponse.ok(
//                        "Node created successfully", nodeService.createNode(node)
//                )
//        );
//    }

}
