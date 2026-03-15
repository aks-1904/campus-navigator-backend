# Campus Navigation and Accessibility Planner

## Overview

Campus Navigation and Accessibility Planner is a web-based system
designed to help users efficiently navigate large campus environments
such as universities, institutes, or corporate campuses. The system
provides optimal routes between different campus locations such as
buildings, classrooms, entrances, washrooms, canteens, gardens, and
other facilities.

The application dynamically models the campus as a graph structure where
locations are represented as nodes and paths between them are
represented as edges. This allows routing algorithms to compute the most
efficient navigation paths.

Unlike static campus maps, the system includes an admin module that
allows administrators to dynamically add new buildings, entrances,
facilities, and pathways without modifying the application code. This
makes the system scalable as campuses expand.

The project demonstrates the application of Design and Analysis of
Algorithms (DAA) concepts such as graph traversal and shortest path
algorithms in a real-world system.

------------------------------------------------------------------------

## Key Features

-   Dynamic campus graph structure
-   Admin panel to manage campus infrastructure
-   Support for multiple building entrances
-   Ability to add facilities such as washrooms, canteens, gardens, and
    lifts
-   Path creation between any two campus nodes
-   Graph-based navigation system
-   Modular backend architecture for future expansion
-   Integration of routing algorithms such as:
    -   Dijkstra's Algorithm
    -   Breadth First Search (BFS)
    -   Floyd-Warshall Algorithm

------------------------------------------------------------------------

## Tech Stack

### Backend

-   Java
-   Spring Boot
-   Spring Data JPA
-   PostgreSQL

### Frontend (Planned)

-   React.js
-   REST API Integration

### Algorithms

-   Graph Data Structure
-   Dijkstra Shortest Path
-   Breadth First Search
-   Floyd-Warshall

------------------------------------------------------------------------

## System Architecture

The backend follows a layered architecture to ensure modularity and
maintainability.

Controller Layer → Service Layer → Repository Layer → Database

The routing system builds an in-memory graph structure using data stored
in the database and executes pathfinding algorithms on this graph.

------------------------------------------------------------------------

## Database Design

### Node Table

Represents all locations within the campus such as: - Buildings -
Entrances - Classrooms - Washrooms - Canteens - Gardens - Junctions

Attributes: - id - name - type - latitude - longitude

### Edge Table

Represents paths between nodes.

Attributes: - id - source_node - destination_node - distance -
accessibility attributes

------------------------------------------------------------------------

## Project Structure

src/main/java/com/campusnavigator

controller\
AdminController.java

service\
NodeService.java\
EdgeService.java\
GraphService.java

repository\
NodeRepository.java\
EdgeRepository.java

model\
Node.java\
Edge.java

graph\
Graph.java\
DijkstraAlgorithm.java\
BFSAlgorithm.java\
FloydWarshallAlgorithm.java

config\
SecurityConfig.java

exception\
GlobalExceptionHandler.java

------------------------------------------------------------------------

## Setup Instructions

### 1. Clone the Repository

git clone https://github.com/aks-1904/campus-navigator.git

### 2. Open the Project

Open the project in IntelliJ IDEA or any Java IDE.

### 3. Install PostgreSQL

Create database:

CREATE DATABASE campus_navigation;

### 4. Configure application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/campus_navigation\
spring.datasource.username=postgres\
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update\
spring.jpa.show-sql=true

server.port=8080

### 5. Install Dependencies

mvn clean install

### 6. Run the Application

mvn spring-boot:run

Or run CampusNavigationApplication.java from the IDE.

------------------------------------------------------------------------

## Future Enhancements

-   User navigation module
-   Interactive campus map
-   Route visualization
-   Accessibility-aware routing
-   Multi-route recommendations
-   Mobile support

------------------------------------------------------------------------

## Project Motivation

Large campuses often lack efficient digital navigation systems. Static
maps quickly become outdated as new buildings and facilities are added.
This project solves this problem by creating a dynamic graph-based
campus navigation system that can evolve as the campus grows.

------------------------------------------------------------------------

## License

This project is developed for academic purposes as part of a Design and
Analysis of Algorithms (DAA) Project-Based Learning project.