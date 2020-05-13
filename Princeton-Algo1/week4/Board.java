import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final char[] tiles;
    private final char dim;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dim = (char) tiles.length;
        this.tiles = new char[dim * dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.tiles[i * dim + j] = (char) tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        int size = (int) Math.log10(dim*dim-1) + 2;
        StringBuilder output = new StringBuilder();
        output.append((int) dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                output.append(String.format("%"+size+"d", (int) tiles[i * dim + j]));
            }
            output.append("\n");
        }
        return output.toString();
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dim * dim; i++) {
            if (tiles[i] != 0 && tiles[i] != i + 1)
                hamming++;
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int value = tiles[i*dim+j];
                if (value != 0) {
                    int correctRow = (value-1) / dim;
                    int correctColumn = (value-1) - correctRow*dim;
                    manhattan += Math.abs(i-correctRow) + Math.abs(j-correctColumn);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dim * dim - 1; i++) {
            if (tiles[i] != i + 1)
                return false;
        }
        if (tiles[dim * dim - 1] != 0)
            return false;
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dim != that.dim)
            return false;
        for (int i = 0; i < dim * dim; i++) {
            if (this.tiles[i] != that.tiles[i])
                return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int blankRow = 0;
        int blankCol = 0;
        for (int i = 0; i < dim*dim; i++) {
            if (tiles[i] == 0) {
                blankRow = i / dim;
                blankCol = i - blankRow*dim;
                break;
            }
        }
        if (blankRow != 0) {
            int[][] newTiles = cloneBoard();
            newTiles[blankRow][blankCol] = tiles[(blankRow-1)*dim+blankCol];
            newTiles[blankRow-1][blankCol] = 0;
            neighbors.push(new Board(newTiles));
        }
        if (blankRow != dim-1) {
            int[][] newTiles = cloneBoard();
            newTiles[blankRow][blankCol] = tiles[(blankRow+1)*dim+blankCol];
            newTiles[blankRow+1][blankCol] = 0;
            neighbors.push(new Board(newTiles));
        }
        if (blankCol != 0) {
            int[][] newTiles = cloneBoard();
            newTiles[blankRow][blankCol] = tiles[blankRow*dim+blankCol-1];
            newTiles[blankRow][blankCol-1] = 0;
            neighbors.push(new Board(newTiles));
        }
        if (blankCol != dim-1) {
            int[][] newTiles = cloneBoard();
            newTiles[blankRow][blankCol] = tiles[blankRow*dim+blankCol+1];
            newTiles[blankRow][blankCol+1] = 0;
            neighbors.push(new Board(newTiles));
        }
        return neighbors;
    }

    private int[][] cloneBoard() {
        int[][] newTiles = new int[dim][dim];
        for (int j = 0; j < dim*dim; j++) {
            int currentRow = j / dim;
            int currentCol = j - currentRow*dim;
            newTiles[currentRow][currentCol] = tiles[j];
        }
        return newTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(cloneBoard());
        int first = 0;
        while (twin.tiles[first] == 0) first++;
        int second = first + 1;
        while (twin.tiles[second] == 0) second++;
        
        char temp = twin.tiles[first];
        twin.tiles[first] = twin.tiles[second];
        twin.tiles[second] = temp;

        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] test = {{0,1,3},{4,8,2},{7,6,5}};
        int[][] test = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                test[i][j] = i*10+j;
            }
        }
        Board board = new Board(test);
        StdOut.println("Test board creation");
        StdOut.println(board);
        StdOut.println("Hamming: " + board.hamming());
        StdOut.println("Manhattan: " + board.manhattan());
        StdOut.println("Is goal? " + board.isGoal());
        StdOut.println("Self-equal? " + board.equals(board));
        StdOut.println("Test twin()");
        StdOut.println(board.twin());
        StdOut.println("Test neighbors()");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}