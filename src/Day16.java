import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.PriorityQueue;

public class Day16 {

    // directions: 0 north, 1 east, 2 south, 3 west
    static final int[] deltaX = {-1, 0, 1, 0};
    static final int[] deltaY = {0, 1, 0, -1};

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./src/resources/day16_input.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[][] maze = reader.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        printMatrix(maze);

        var startPosition = new Position();
        var endPosition = new Position();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 'S') {
                    startPosition.setRow(i);
                    startPosition.setCol(j);
                } else if (maze[i][j] == 'E') {
                    endPosition.setRow(i);
                    endPosition.setCol(j);
                }
            }
        }

        System.out.println("Lowest reindeer walk score: " + findLowestScore(maze, startPosition, endPosition));
    }

    // Dijkstra's algorithm
    public static int findLowestScore(char[][] maze, Position start, Position end) {
        // queue to store states
        // priority: lowest accumulated cost
        PriorityQueue<State> stateQueue = new PriorityQueue<>();
        boolean[][][] visited = new boolean[maze.length][maze[0].length][4];

        // adds initial state (S position, east direction (1), 0 cost)
        stateQueue.add(new State(start.getRow(), start.getCol(), 1, 0));

        while (!stateQueue.isEmpty()) {
            // removes state with the lowest cost from queue
            State currentState = stateQueue.poll();

            // if already visited position in that direction, continues to next iteration
            if (visited[currentState.x][currentState.y][currentState.direction]) {
                continue;
            }

            // mark as visited
            visited[currentState.x][currentState.y][currentState.direction] = true;

            // checks if it's end position
            if (currentState.x == end.getRow() && currentState.y == end.getCol()) {
                return currentState.cost;
            }

            // possible movements:
            // forward on current direction (cost + 1)
            int nx = currentState.x + deltaX[currentState.direction];
            int ny = currentState.y + deltaY[currentState.direction];
            if (nx >= 0 && ny >= 0 && nx < maze.length && ny < maze[0].length // in bounds
                    && maze[nx][ny] != '#') { // if next tile isn't a wall
                // adds to queue with +1 cost
                stateQueue.add(new State(nx, ny, currentState.direction, currentState.cost + 1));
            }

            // rotate to the left (cost + 1000)
            int leftDirection = (currentState.direction + 3) % 4; // directions: 0 north, 1 east, 2 south, 3 west
            stateQueue.add(new State(currentState.x, currentState.y, leftDirection, currentState.cost + 1000));

            // rotate to the right (cost + 1000)
            int rightDirection = (currentState.direction + 1) % 4; // directions: 0 north, 1 east, 2 south, 3 west
            stateQueue.add(new State(currentState.x, currentState.y, rightDirection, currentState.cost + 1000));
        }

        return -1;
    }

    // State class represents current states
    // Comparable<State> -> so priorityQueue orders by cost (lowest first)
    static class State implements Comparable<State> {
        int x;
        int y;
        int direction;
        int cost;

        public State(int x, int y, int direction, int cost) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.cost = cost;
        }

        @Override
        public int compareTo(State o) {
            return Integer.compare(this.cost, o.cost); // defines that class should be ordered by cost property
        }
    }

    private static void printMatrix(char[][] matrix) {
        for (var values : matrix) {
            for (char value : values) {
                System.out.print(value);
            }
            System.out.println();
        }
        System.out.println();
    }

}