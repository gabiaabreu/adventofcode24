package utils;

import java.util.List;

// State class represents current states
// Comparable<State> -> so priorityQueue orders by cost (lowest first)
public class State implements Comparable<State> {
    int x;
    int y;
    int direction;
    int cost;
    List<Position> positions;

    public State () {

    }

    public State(int x, int y, int direction, int cost, List<Position> positions) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.cost = cost;
        this.positions = positions;
    }

    public State(int x, int y, int cost, List<Position> positions) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.positions = positions;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public int compareTo(State o) {
        return Integer.compare(this.cost, o.cost); // defines that class should be ordered by cost property
    }
}