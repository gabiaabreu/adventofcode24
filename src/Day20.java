import utils.Position;
import utils.State;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Day20 {

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

        var resultWithoutCheat = startRaceBFS(racetrack, startPosition, endPosition);
        System.out.println("It takes " + resultWithoutCheat.getCost() + " picoseconds to finish race without cheating");

        // part one - cheat removing 1 wall
        var cheatCount = 0;
        for (var wall : walls) {
            racetrack[wall.getRow()][wall.getCol()] = '.';

            var minTimeCheating = startRaceBFS(racetrack, startPosition, endPosition).getCost();
            if (resultWithoutCheat.getCost() - minTimeCheating >= 100) {
                cheatCount++;
            }

            racetrack[wall.getRow()][wall.getCol()] = '#';
        }

        System.out.println(cheatCount + " cheats could save me at least 100 picoseconds");

        // part two
        int[][] distances = startRaceDijkstra(racetrack, startPosition, endPosition);
        List<Position> path = resultWithoutCheat.getPositions();

        int biggerCheats = 0;
        for (int i = 0; i < path.size(); i++) {
            var posA = path.get(i);
            var distanceFromStartToA = distances[posA.getRow()][posA.getCol()];

            for (int j = i + 1; j < path.size(); j++) {
                var posB = path.get(j);
                var distanceFromStartToB = distances[posB.getRow()][posB.getCol()];

                    int manhattanDistance = Math.abs(posA.getRow() - posB.getRow())
                            + Math.abs(posA.getCol() - posB.getCol());

                    if (manhattanDistance <= 20) {
                        var distanceFromBToEnd = resultWithoutCheat.getCost() - distanceFromStartToB;
                        var totalDistance = distanceFromStartToA + manhattanDistance + distanceFromBToEnd;

                        if (resultWithoutCheat.getCost() - totalDistance >= 100) {
                            biggerCheats++;
                        }
                    }
            }
        }

        System.out.println(biggerCheats + " cheats would save me at least 100ps");
    }

    private static State startRaceBFS(char[][] map, State initialState, State targetState) {
        var startX = initialState.getX();
        var startY = initialState.getY();

        Queue<State> queue = new LinkedList<>();
        boolean[][] visited = new boolean[map.length][map[0].length];

        queue.offer(new State(startX, startY, 0, new ArrayList<>()));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.getX() == targetState.getX() && current.getY() == targetState.getY()) {
                return current;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];
                var currentPositions = current.getPositions();

                if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length
                        && map[newX][newY] != '#' && !visited[newX][newY]) {
                    visited[newX][newY] = true;

                    currentPositions.add(new Position(newX, newY));
                    queue.offer(new State(newX, newY, current.getCost() + 1, currentPositions));
                }
            }
        }

        return new State(-1, -1, -1);
    }

    // I dont need Dijkstra to find the shortest path because I know there's only 1 path
    // but it's useful to store nodes distance from starting point
    private static int[][] startRaceDijkstra(char[][] racetrack, State initialState, State targetState) {
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
                return trackTime;
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

        return new int[][]{};
    }
}
