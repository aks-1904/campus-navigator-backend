package org.akshay.campusnavigator.controller;

import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.enums.NodeType;
import org.akshay.campusnavigator.service.GraphService;
import org.akshay.campusnavigator.service.NodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final NodeService nodeService;
    private final GraphService graphService;

    public UserController(NodeService nodeService, GraphService graphService) {
        this.nodeService = nodeService;
        this.graphService = graphService;
    }

    @GetMapping("/node")
    public ResponseEntity <
            ResponseDTOs.ApiResponse<
                    List<ResponseDTOs.NodeResponse>
                    >
            > getAllNodes(){
        return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        null, nodeService.getAllNodes()
                )
        );
    }

    @GetMapping("/node/type/{type}")
    public ResponseEntity<
            ResponseDTOs.ApiResponse<
                    List<ResponseDTOs.NodeResponse>
                    >
            > getNodesByType(@PathVariable NodeType type){
        return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        null, nodeService.getNodesByType(type)
                )
        );
    }

    @GetMapping("/graph")
    public ResponseEntity<
            ResponseDTOs.ApiResponse<
                    ResponseDTOs.GraphResponse
                    >
            > getGraph(){
        return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        null, graphService.getGraph()
                )
        );
    }

    @GetMapping("/graph/shortest-path/{sourceNodeId}/{destinationNodeId}")
    public ResponseEntity<
            ResponseDTOs.ApiResponse<
                    List<ResponseDTOs.EdgeResponse>
                    >
            > getShortestPath(@PathVariable Long sourceNodeId, @PathVariable Long destinationNodeId){
         return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        null, graphService.getShortestPath(sourceNodeId,destinationNodeId)
                )
        );
    }

    @GetMapping("/graph/all/shortest-path/{sourceNodeId}/{destinationNodeId}")
    public ResponseEntity<
            ResponseDTOs.ApiResponse<
                    List<ResponseDTOs.EdgeResponse>
                    >
            > getAllShortestPath(@PathVariable Long sourceNodeId, @PathVariable Long destinationNodeId){
        return ResponseEntity.ok(
                ResponseDTOs.ApiResponse.ok(
                        null, graphService.getAllShortestPath(sourceNodeId,destinationNodeId)
                )
        );
    }

}
