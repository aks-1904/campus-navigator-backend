package org.akshay.campusnavigator.repository;

import org.akshay.campusnavigator.model.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {
    Optional<Edge> findBySourceNodeIdAndDestinationNodeId(Long sourceNodeId, Long destinationNodeId);
}
