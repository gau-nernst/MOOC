import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private SET<Point2D> points;
    
    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> result = new Stack<>();
        for (Point2D point : points) {
            if (rect.contains(point)) result.push(point);
        }

        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Point2D minPoint = null;
        double minDistance = 2;
        for (Point2D point : points) {
            double distance = p.distanceSquaredTo(point);
            if (distance < minDistance) {
                minPoint = point;
                minDistance = distance;
            }
        }

        return minPoint;
    }
 
    public static void main(String[] args) {

    }                 
 }