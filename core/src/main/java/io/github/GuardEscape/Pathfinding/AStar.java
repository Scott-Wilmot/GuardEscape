package io.github.GuardEscape.Pathfinding;

import java.util.ArrayDeque;
import java.util.PriorityQueue;

public class AStar {

    ArrayDeque<Node> path;
    PriorityQueue<Node> pq;
    Node start, finish;

    public AStar(Node start, Node finish) {
        this.start = start;
        this.finish = finish;

        path = new ArrayDeque<>();
        pq = new PriorityQueue<>(
            (node1, node2) -> {
                int h1 = heuristic(node1, finish);
                int h2 = heuristic(node2, finish);

                return Integer.compare(h1, h2);
            }
        );

        findPath(start, finish);
    }

    public boolean findPath(Node start, Node finish) {
        // Clear pq
        pq.clear();

        // Initialize start node
        pq.add(start);
        start.setParent(null);
        start.setCost(1);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Path found
            if (current.equals(finish)) return true;

            for (Node neighbor : current.getNeighbors()) {
                int tentativeCost = current.getCost() + 1;
                int neighborCost = neighbor.getCost();
                if (tentativeCost < neighborCost) {
                    neighbor.setParent(current);
                    neighbor.setCost(tentativeCost);
                    if (!pq.contains(neighbor))
                        pq.add(neighbor);
                }
            }
        }
        return false;   // Path not found
    }

    public ArrayDeque<Node> getPath() {
        path.clear();
        Node current = finish;
        while (current.getParent() != null) {
            path.add(current);
            current = current.getParent();
        }
        return path;
    }

    public int heuristic(Node current, Node finish) {
        return Math.abs(current.getX() - finish.getX()) + Math.abs(current.getY() + finish.getY());
    }

}
