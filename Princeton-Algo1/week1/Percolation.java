import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] open;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private int numOpen;
    private final int n;
    private final int top;
    private final int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.uf = new WeightedQuickUnionUF(n*n+2);
        this.ufFull = new WeightedQuickUnionUF(n*n+1);
        this.open = new boolean[n][n];
        this.numOpen = 0;
        this.n = n;
        this.top = n*n;
        this.bottom = n*n+1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                open[i][j] = false;
            }
        }
    }

    private int toFlatIndex(int row, int col) {
        return (row-1)*this.n + col-1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) throw new IllegalArgumentException();

        // if not open yet
        if (!isOpen(row, col)) {
            this.open[row-1][col-1] = true;
            this.numOpen += 1;

            // top row
            if (row == 1) {
                this.uf.union(toFlatIndex(row, col), this.top);
                this.ufFull.union(toFlatIndex(row, col), this.top);
            }
            // bottom row
            if (row == this.n) {
                this.uf.union(toFlatIndex(row, col), this.bottom);
            }

            // top
            if (row > 1 && isOpen(row-1, col)) {
                this.uf.union(toFlatIndex(row, col), toFlatIndex(row-1, col));
                this.ufFull.union(toFlatIndex(row, col), toFlatIndex(row-1, col));
            }
            // right
            if (col < n && isOpen(row, col+1)) {
                this.uf.union(toFlatIndex(row, col), toFlatIndex(row, col+1));
                this.ufFull.union(toFlatIndex(row, col), toFlatIndex(row, col+1));
            }
            // left
            if (col > 1 && isOpen(row, col-1)) {
                this.uf.union(toFlatIndex(row, col), toFlatIndex(row, col-1));
                this.ufFull.union(toFlatIndex(row, col), toFlatIndex(row, col-1));
            }
            // bottom
            if (row < n && isOpen(row+1, col)) {
                this.uf.union(toFlatIndex(row, col), toFlatIndex(row+1, col));
                this.ufFull.union(toFlatIndex(row, col), toFlatIndex(row+1, col));
            }
            
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) throw new IllegalArgumentException();

        return this.open[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) throw new IllegalArgumentException();
        
        return this.ufFull.find(this.top) == this.ufFull.find(toFlatIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.uf.find(this.top) == this.uf.find(this.bottom);
    }

    // test client (optional)
    // public static void main(String[] args) {
        
    // }
}