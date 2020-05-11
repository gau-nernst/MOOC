import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class FastCollinearPoints {
    private final Point[] points;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
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

        processData();
    }

    private void processData() {
        Stack<Point[]> segmentStack = new Stack<>();
        
        for (int p = 0; p < points.length; p++) {
            Point[] pointSlopes = new Point[points.length];
            for (int i = 0; i < points.length; i++) pointSlopes[i] = points[i];

            Arrays.sort(pointSlopes, points[p].slopeOrder());

            double[] slopes = new double[pointSlopes.length];
            for (int i = 1; i < pointSlopes.length; i++) slopes[i] = pointSlopes[0].slopeTo(pointSlopes[i]);

            int q = 1;
            int count = 1;

            while (q < pointSlopes.length) {
                if (q == pointSlopes.length-1 || slopes[q] != slopes[q+1]) {
                    if (count > 2) {
                        // from q, q-1, ... q-count+1 and 0
                        Point[] pointOrders = new Point[count+1];
                        pointOrders[count] = pointSlopes[0];
                        for (int j = 0; j < count; j++) pointOrders[j] = pointSlopes[q-j];
                        Arrays.sort(pointOrders);
                       
                        Point[] newSegment = new Point[2];
                        newSegment[0] = pointOrders[0];
                        newSegment[1] = pointOrders[count];

                        Boolean isIn = false;
                        for (Point[] segment : segmentStack) {
                            if (segment[0] == newSegment[0] && segment[1] == newSegment[1]) {
                                isIn = true;
                                break;
                            }
                        }
                        if (!isIn) {
                            segmentStack.push(newSegment);
                        }
                    }
                    count = 1;
                    q++;
                    continue;
                }
                count++;
                q++;
                continue;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}