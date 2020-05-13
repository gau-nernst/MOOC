import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Stack<Board> solution;

    private class Node implements Comparable<Node> {
        Board board;
        int priority;
        int manhattan;
        int moves;
        Node previous;
        public Node(Board board, int moves, Node previous) {
            this.board = board;
            manhattan = board.manhattan();
            priority = manhattan + moves;
            this.moves = moves;
            this.previous = previous;
        }

        public int compareTo(Node that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return +1;
            if (this.manhattan < that.manhattan) return -1;
            if (this.manhattan > that.manhattan) return +1;
            return 0;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<>();
        Node firstNode = new Node(initial, 0, null);
        pq.insert(firstNode);
        
        Board initial2 = initial.twin();
        MinPQ<Node> pq2 = new MinPQ<>();
        Node firstNode2 = new Node(initial2, 0, null);
        pq2.insert(firstNode2);

        Node search;
        Node search2;
        
        while (true) {
            search = pq.delMin();

            for (Board neighbor : search.board.neighbors()) {
                if(search.previous == null || !neighbor.equals(search.previous.board)) {
                    Node newNode = new Node(neighbor, search.moves+1, search);
                    pq.insert(newNode);
                }
            }

            if (search.board.isGoal()) break;

            // Check for twin board
            search2 = pq2.delMin();

            for (Board neighbor : search2.board.neighbors()) {
                if(search.previous == null || !neighbor.equals(search2.previous.board)) {
                    Node newNode = new Node(neighbor, search2.moves+1, search2);
                    pq2.insert(newNode);
                }
            }
            
            if (search2.board.isGoal()) break;
        }

        solution = new Stack<>();
        if (search.board.isGoal()) {
            while (search.previous != null) {
                solution.push(search.board);
                search = search.previous;
            }
            solution.push(search.board);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (solution.size() == 0) return false;
        return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (solution.size() == 0) return null;
        Queue<Board> temp = new Queue<>();
        for (Board board : solution) {
            temp.enqueue(board);
        }
        return temp;
    }

    // test client (see below) 
    public static void main(String[] args) {
    // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}