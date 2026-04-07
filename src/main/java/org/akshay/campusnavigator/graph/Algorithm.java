package org.akshay.campusnavigator.graph;

import java.util.*;

public class Algorithm {

    public static class Dijkstra {

        public static List<Integer> shortestPath(Graph graph, int source, int destination) {
            int V = graph.vertices;
            double[] dist = new double[V];
            int[] parent = new int[V];
            boolean[] visited = new boolean[V];

            Arrays.fill(dist, Double.MAX_VALUE);
            Arrays.fill(parent, -1);

            PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
                    Comparator.comparingDouble(n -> n.distance)
            );
            dist[source] = 0;

            pq.add(new NodeDistance(source, 0));

            while (!pq.isEmpty()) {
                NodeDistance current = pq.poll();
                int u = current.node;

                if (visited[u])
                    continue;
                visited[u] = true;

                AdjacencyListNode neighbour = graph.getNeighbours(u);

                while (neighbour != null) {
                    int v = neighbour.destinationNode.intValue();
                    double weight = neighbour.distance;

                    if (!visited[v] && dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        parent[v] = u;

                        pq.add(new NodeDistance(v, dist[v]));
                    }
                    neighbour = neighbour.next;
                }
            }

            return reconstructPath(parent, source, destination);
        }

        private static List<Integer> reconstructPath(int[] parent, int source, int destination) {
            List<Integer> path = new ArrayList<>();

            for (int v = destination; v != -1; v = parent[v])
                path.add(v);

            Collections.reverse(path);

            if (path.get(0) != source)
                return new ArrayList<>(); // No path

            return path;

        }

        private static class NodeDistance {
            int node;
            double distance;

            public NodeDistance(int node, double distance) {
                this.distance = distance;
                this.node = node;
            }
        }
    }

    public static class FloydWarshall {

        private static final double INF = Double.MAX_VALUE;

        public static Map<Integer, Map<Integer, List<Integer>>> shortestPaths(Graph graph) {

            int V = graph.vertices;

            double[][] dist = new double[V][V];
            int[][] next = new int[V][V];

            // Initialize matrices
            for (int i = 0; i < V; ++i) {
                for (int j = 0; j < V; ++j) {

                    if (i == j)
                        dist[i][j] = 0;
                    else
                        dist[i][j] = INF;

                    next[i][j] = -1;
                }
            }

            // Convert adjacency list -> matrix
            for (int i = 0; i < V; ++i) {

                AdjacencyListNode node = graph.getNeighbours(i);

                while (node != null) {

                    int v = node.destinationNode.intValue();
                    double weight = node.distance;

                    dist[i][v] = weight;
                    next[i][v] = v;

                    node = node.next;
                }
            }

            // Floyd Warshall core
            for (int k = 0; k < V; ++k) {
                for (int i = 0; i < V; ++i) {
                    for (int j = 0; j < V; ++j) {

                        if (dist[i][k] == INF || dist[k][j] == INF)
                            continue;

                        if (dist[i][k] + dist[k][j] < dist[i][j]) {

                            dist[i][j] = dist[i][k] + dist[k][j];
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }

            // Build all paths
            Map<Integer, Map<Integer, List<Integer>>> allPaths = new HashMap<>();

            for (int i = 0; i < V; i++) {

                Map<Integer, List<Integer>> destinationPaths = new HashMap<>();

                for (int j = 0; j < V; j++) {

                    if (i == j)
                        continue;

                    List<Integer> path = reconstructPath(i, j, next);

                    if (!path.isEmpty()) {
                        destinationPaths.put(j, path);
                    }
                }

                allPaths.put(i, destinationPaths);
            }

            return allPaths;
        }


        private static List<Integer> reconstructPath(int source, int destination, int[][] next) {

            if (next[source][destination] == -1)
                return new ArrayList<>();

            List<Integer> path = new ArrayList<>();
            path.add(source);

            while (source != destination) {

                source = next[source][destination];
                path.add(source);
            }

            return path;
        }
    }

    public static List<Integer> reconstructPath(int source, int destination, Result result) {
        if (result.next[source][destination] == -1) {
            return new ArrayList<>();
        }

        List<Integer> path = new ArrayList<>();
        path.add(source);

        while (source != destination) {
            source = result.next[source][destination];
            path.add(source);
        }

        return path;
    }

    public static class Result {
        public double[][] dist;
        public int[][] next;

        public Result(double[][] dist, int[][] next) {
            this.dist = dist;
            this.next = next;
        }
    }

    public static class BFS {
        public static List<Integer> shortestPath(Graph graph, int source, int destination) {
            int V = graph.vertices;

            boolean[] visited = new boolean[V];
            int[] parent = new int[V];

            Arrays.fill(parent, -1);

            Queue<Integer> queue = new LinkedList<>();

            visited[source] = true;
            queue.add(source);

            while (!queue.isEmpty()) {
                int current = queue.poll();

                if (current == destination)
                    break;

                AdjacencyListNode neighbour = graph.getNeighbours(current);

                while (neighbour != null) {
                    int v = neighbour.destinationNode.intValue();

                    if (!visited[v]) {
                        visited[v] = true;
                        parent[v] = current;
                        queue.add(v);
                    }

                    neighbour = neighbour.next;
                }
            }

            return reconstructPath(parent, source, destination);
        }

        private static List<Integer> reconstructPath(int[] parent, int source, int destination) {

            List<Integer> path = new ArrayList<>();

            for (int v = destination; v != -1; v = parent[v]) {
                path.add(v);
            }

            Collections.reverse(path);

            if (path.get(0) != source) {
                return new ArrayList<>();
            }

            return path;

        }
    }
}
