import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private final Point[] points;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        this.points = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) 
                throw new IllegalArgumentException();
            
            for (int j = 0; j < i; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }

            this.points[i] = points[i];
        }

        if (this.points.length < 4) {
            segments = new LineSegment[0];
        } else processData();
    }

    private void processData() {
    
        Stack<Point[]> segmentStack = new Stack<>();

        for (int a = 3; a < points.length; a++) {
            for (int b = 2; b < a; b++) {
                for (int c = 1; c < b; c++) {
                    for (int d = 0; d < c; d++) {
                        double slopeB = points[a].slopeTo(points[b]);
                        double slopeC = points[a].slopeTo(points[c]);
                        double slopeD = points[a].slopeTo(points[d]);

                        if (slopeB == slopeC && slopeC == slopeD) {
                            Point[] segmentPoints = new Point[4];
                            segmentPoints[0] = points[a];
                            segmentPoints[1] = points[b];
                            segmentPoints[2] = points[c];
                            segmentPoints[3] = points[d];
                            Arrays.sort(segmentPoints);

                            Point[] newSegment = new Point[2];
                            newSegment[0] = segmentPoints[0];
                            newSegment[1] = segmentPoints[3];

                            segmentStack.push(newSegment);
                            
                        }   
                    }
                }
            }
        }

        int size = segmentStack.size();
        segments = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            Point[] segment = segmentStack.pop();
            segments[i] = new LineSegment(segment[0], segment[1]);
        }
        
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        LineSegment[] returnSegments = new LineSegment[segments.length];
        for (int i = 0; i < segments.length; i++) returnSegments[i] = segments[i];
        return returnSegments;
    }

    public static void main(String[] args) {
        
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}