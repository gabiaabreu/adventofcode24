import utils.Position;
import utils.State;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Day20 {

    // each move takes 1 picosecond
    // The goal is to reach the end position as quickly as possible.
    // In this example racetrack, the fastest time is 84 picoseconds.
    // there is only a single path from the start to the end

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1} // (delta x, delta y) up, down, left, right
    };

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./src/resources/day20_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[][] racetrack = reader.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        var startPosition = new State();
        var endPosition = new State();
        for (int i = 0; i < racetrack.length; i++) {
            for (int j = 0; j < racetrack[0].length; j++) {
                if (racetrack[i][j] == 'S') {
                    startPosition.setX(i);
                    startPosition.setY(j);
                } else if (racetrack[i][j] == 'E') {
                    endPosition.setX(i);
                    endPosition.setY(j);
                }
            }
        }

        List<Position> positions = new ArrayList<>();
        startPosition.setPositions(positions);
        endPosition.setPositions(positions);

        var minTimeWithoutCheat = startRace(racetrack, startPosition, endPosition);
        System.out.println("It takes " + minTimeWithoutCheat + " picoseconds to finish race without cheating");
    }

    private static int startRace(char[][] racetrack, State initialState, State targetState) {
        int[][] trackTime = new int[racetrack.length][racetrack[0].length];
        for (int[] row : trackTime) Arrays.fill(row, Integer.MAX_VALUE); // assign "infinity" to all nodes

        trackTime[initialState.getX()][initialState.getY()] = 0;

        PriorityQueue<State> stateQueue = new PriorityQueue<>();
        stateQueue.offer(initialState);

        while (!stateQueue.isEmpty()) {
            var currentState = stateQueue.poll();
            var x = currentState.getX();
            var y = currentState.getY();

            if (x == targetState.getX() && y == targetState.getY()) {
                return currentState.getCost();
            }

            for (int[] direction : DIRECTIONS) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (newX >= 0 && newY >= 0 && newX < racetrack.length && newY < racetrack[0].length // in bounds
                        && racetrack[newX][newY] != '#') { // not wall
                    // adds 1 step to current distance
                    int timeTaken = currentState.getCost() + 1;

                    // if lower than cost currently assigned to node, assign new value
                    if (timeTaken < trackTime[newX][newY]) {
                        trackTime[newX][newY] = timeTaken;

                        // adds to positions visited list
                        List<Position> newPositions = new ArrayList<>(currentState.getPositions());
                        newPositions.add(new Position(newX, newY));

                        // add new state
                        stateQueue.offer(new State(newX, newY, timeTaken, newPositions));
                    }
                }
            }
        }

        return -1;
    }
}
