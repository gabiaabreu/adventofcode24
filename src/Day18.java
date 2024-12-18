import utils.Position;
import utils.State;
import utils.MatrixUtils;

import java.io.*;
import java.util.*;

public class Day18 {

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1} // (delta x, delta y) up, down, left, right
    };

    private static final int SIZE = 71;

    public static void main(String[] args) throws IOException {
        File file = new File("./src/resources/day18_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<Position> corruptedBytes = reader.lines().map(l -> {
            var i = Integer.parseInt(l.split(",")[1]);
            var j = Integer.parseInt(l.split(",")[0]);

            return new Position(i, j);
        }).toList();

        var firstBytes = corruptedBytes.subList(0, 1024);

        char[][] memoryMap = new char[SIZE][SIZE];
        for (int i = 0; i < memoryMap.length; i++) {
            Arrays.fill(memoryMap[i], '.');

            for (int j = 0; j < memoryMap[0].length; j++) {
                if (firstBytes.contains(new Position(i, j))) {
                    memoryMap[i][j] = '#';
                }
            }
        }

        MatrixUtils.printMatrix(memoryMap);

        List<Position> positions = new ArrayList<>();
        var distance = walkMinimumDistance(memoryMap, new State(0, 0, 0, positions));
        System.out.println("Minimum steps to walk through memory space: " + distance);
    }

    // Dijkstra again!
    private static int walkMinimumDistance(char[][] map, State initialState) {
        int[][] nodesDistance = new int[SIZE][SIZE]; // create nodes matrix
        for (int[] row : nodesDistance) Arrays.fill(row, Integer.MAX_VALUE); // assign "infinity" to all nodes

        // assign 0 to initial node
        nodesDistance[initialState.getX()][initialState.getY()] = 0;

        PriorityQueue<State> stateQueue = new PriorityQueue<>();
        stateQueue.offer(initialState);

        while (!stateQueue.isEmpty()) {
            var currentState = stateQueue.poll(); // poll lowest distance state
            var x = currentState.getX();
            var y = currentState.getY();

            if (x == map.length - 1 && y == map[0].length - 1) { // if got to ending point, return total distance
                return currentState.getCost();
            }

            // walk
            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0]; // deltaX depending on direction
                int newY = y + dir[1]; // deltaY

                if (newX >= 0 && newY >= 0 && newX < map.length && newY < map[0].length // in bounds
                        && map[newX][newY] != '#') { // not wall
                    // adds 1 step to current distance
                    int newDist = currentState.getCost() + 1;

                    // if lower than cost currently assigned to node, assign new value
                    if (newDist < nodesDistance[newX][newY]) {
                        nodesDistance[newX][newY] = newDist;

                        // adds to positions visited list
                        List<Position> newPositions = new ArrayList<>(currentState.getPositions());
                        newPositions.add(new Position(newX, newY));

                        // add new state
                        stateQueue.offer(new State(newX, newY, newDist, newPositions));
                    }
                }
            }
        }

        return -1;
    }
}
