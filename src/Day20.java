import utils.Position;
import utils.State;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

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
        List<Position> walls = new ArrayList<>();
        for (int i = 0; i < racetrack.length; i++) {
            for (int j = 0; j < racetrack[0].length; j++) {
                if (racetrack[i][j] == 'S') {
                    startPosition.setX(i);
                    startPosition.setY(j);
                } else if (racetrack[i][j] == 'E') {
                    endPosition.setX(i);
                    endPosition.setY(j);
                } else if (racetrack[i][j] == '#') {
                    walls.add(new Position(i, j));
                }
            }
        }

        List<Position> positions = new ArrayList<>();
        startPosition.setPositions(positions);
        endPosition.setPositions(positions);

        var minTimeWithoutCheat = startRaceBFS(racetrack, startPosition, endPosition);
        System.out.println("It takes " + minTimeWithoutCheat + " picoseconds to finish race without cheating");

        // part one - cheat removing 1 wall
        var cheatCount = 0;
        for (var wall : walls) {
            racetrack[wall.getRow()][wall.getCol()] = '.';

            var minTimeCheating = startRaceBFS(racetrack, startPosition, endPosition);
            if (minTimeWithoutCheat - minTimeCheating >= 100) {
                cheatCount++;
            }

            racetrack[wall.getRow()][wall.getCol()] = '#';
        }

        System.out.println(cheatCount + " cheats could save me at least 100 picoseconds");
    }

    private static int startRaceBFS(char[][] map, State initialState, State targetState) {
        var startX = initialState.getX();
        var startY = initialState.getY();

        Queue<State> queue = new LinkedList<>();
        boolean[][] visited = new boolean[map.length][map[0].length];

        queue.offer(new State(startX, startY, 0, new ArrayList<>()));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.getX() == targetState.getX() && current.getY() == targetState.getY()) {
                return current.getCost();
            }

            for (int[] dir : DIRECTIONS) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];
                var currentPositions = current.getPositions();
                currentPositions.add(new Position(newX, newY));

                if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length
                        && map[newX][newY] != '#' && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.offer(new State(newX, newY, current.getCost() + 1, currentPositions));
                }
            }
        }

        return -1;
    }

    // I dont need Dijkstra to find the shortest path because I know there's only 1 path
    private static int startRaceDijkstra(char[][] racetrack, State initialState, State targetState) {
        int[][] trackTime = new int[racetrack.length][racetrack[0].length];
        for (int[] row : trackTime) Arrays.fill(row, Integer.MAX_VALUE);

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
                        && racetrack[newX][newY] != '#') {
                    int timeTaken = currentState.getCost() + 1;

                    if (timeTaken < trackTime[newX][newY]) {
                        trackTime[newX][newY] = timeTaken;

                        List<Position> newPositions = new ArrayList<>(currentState.getPositions());
                        newPositions.add(new Position(newX, newY));

                        stateQueue.offer(new State(newX, newY, timeTaken, newPositions));
                    }
                }
            }
        }

        return -1;
    }
}
