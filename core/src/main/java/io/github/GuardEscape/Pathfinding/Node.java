package io.github.GuardEscape.Pathfinding;

import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Node {

    private int x, y;
    private int cost;
    private Array<Node> neighbors;
    private Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        cost = Integer.MAX_VALUE;
        neighbors = new Array<>();
    }

    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }
    public Array<Node> getNeighbors() { return neighbors; }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setParent(Node parent) { this.parent = parent; }
    public Node getParent() { return parent; }

    public void setCost(int cost) { this.cost = cost; }
    public int getCost() { return cost; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Node node = (Node) obj;

        return this.getX() == node.getX() && this.getY() == node.getY();
    }

    public static int getHash(int x, int y) {
        return Objects.hash(x, y);
    }

    @Override
    public int hashCode() {
        return getHash(x, y);
    }

}
