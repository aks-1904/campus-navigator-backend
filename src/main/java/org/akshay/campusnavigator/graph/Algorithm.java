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

            if(path.get(0) != source)
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

    public static class FLoydWarshall {

    }

    public static class BFS {

    }
}
