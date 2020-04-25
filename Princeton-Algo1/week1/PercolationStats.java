import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE95 = 1.96;
    private final double[] threshold;
    private final int t;
    private double mean;
    private double std;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        this.threshold = new double[trials];
        this.t = trials;
        this.mean = -1;
        this.std = -1;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            this.threshold[i] = (double) percolation.numberOfOpenSites() / (n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (this.mean == -1) this.mean = StdStats.mean(this.threshold);
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.std == -1) this.std = StdStats.stddev(this.threshold);
        return this.std;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean - CONFIDENCE95*this.std/Math.sqrt(this.t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean + CONFIDENCE95*this.std/Math.sqrt(this.t);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, t);

        System.out.println("mean\t = " + percolationStats.mean());
        System.out.println("stddev\t = " + percolationStats.stddev());
        System.out.println("95% confidence interval\t = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
        
    }

}