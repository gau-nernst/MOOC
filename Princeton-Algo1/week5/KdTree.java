import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private Point2D minPoint;
    private double minDistance;

    public KdTree() {
        size = 0;
    }

    private static class Node {
        private RectHV rect;
        private Point2D point;
        private Node left, right;
        public Node(Point2D point) {
            this.point = point;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, true, 0.0, 1.0, 0.0, 1.0);
    }

    private Node insert(Node h, Point2D point, boolean vertical, double xmin, double xmax, double ymin, double ymax) {
        if (h == null) {
            size++;
            Node newNode = new Node(point);
            if (vertical) {
                newNode.rect = new RectHV(xmin, ymin, xmax, ymax);
            }
            else {
                newNode.rect = new RectHV(xmin, ymin, xmax, ymax);
            }
            return newNode;
        }

        if(vertical) {
            if      (point.x() < h.point.x())       h.left  = insert(h.left,  point, !vertical, xmin, h.point.x(), ymin, ymax);
            else if (h.point.compareTo(point) != 0) h.right = insert(h.right, point, !vertical, h.point.x(), xmax, ymin, ymax);
        } else {
            if      (point.y() < h.point.y())       h.left  = insert(h.left,  point, !vertical, xmin, xmax, ymin, h.point.y());
            else if (h.point.compareTo(point) != 0) h.right = insert(h.right, point, !vertical, xmin, xmax, h.point.y(), ymax);
        }

        return h;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node h = root;
        boolean vertical = true;
        while (h != null) {
            if (vertical) {
                if (p.x() < h.point.x()) h = h.left;
                else if (p.x() > h.point.x()) h = h.right;
                else if (p.compareTo(h.point) == 0) return true;
                else h = h.right;
            } else {
                if (p.y() < h.point.y()) h = h.left;
                else if (p.y() > h.point.y()) h = h.right;
                else if (p.compareTo(h.point) == 0) return true;
                else h = h.right;
            }
            vertical = !vertical;
        }
        return false;
    }

    public void draw() {
        drawNode(root, true);
    }

    private void drawNode(Node h, boolean vertical) {
        if (h == null) return;

        StdDraw.setPenRadius(0.005);
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.point.x(), h.rect.ymin(), h.point.x(), h.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(h.rect.xmin(), h.point.y(), h.rect.xmax(), h.point.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        h.point.draw();

        drawNode(h.left, !vertical);
        drawNode(h.right, !vertical);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> result = new Queue<>();
        range(root, rect, result);
        
        return result;
    }

    private void range(Node h, RectHV rect, Queue<Point2D> result) {
        if (h == null || !rect.intersects(h.rect)) return;
        if (rect.contains(h.point)) result.enqueue(h.point);

        range(h.right, rect, result);
        range(h.left, rect, result);
        
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        minPoint = new Point2D(root.point.x(), root.point.y());
        minDistance = p.distanceSquaredTo(minPoint);
        nearest(root, p, true);
        return new Point2D(minPoint.x(), minPoint.y());
    }

    private void nearest(Node h, Point2D p, boolean vertical) {
        if (h == null || h.rect.distanceSquaredTo(p) > minDistance) return;

        double distance = p.distanceSquaredTo(h.point);
        if (distance < minDistance) {
            minPoint = new Point2D(h.point.x(), h.point.y());
            minDistance = distance;
        }

        if (vertical) {
            if (p.x() < h.point.x()) {
                nearest(h.left, p, !vertical);
                nearest(h.right, p, !vertical);
            } else {
                nearest(h.right, p, !vertical);
                nearest(h.left, p, !vertical);
            }
        } else {
            if (p.y() < h.point.y()) {
                nearest(h.left, p, !vertical);
                nearest(h.right, p, !vertical);
            } else {
                nearest(h.right, p, !vertical);
                nearest(h.left, p, !vertical);
            }
        }

    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        // tree.insert(new Point2D(0.4,0.5));
        // tree.insert(new Point2D(0.8,0.3));
        // tree.insert(new Point2D(0.2,0.2));
        // tree.insert(new Point2D(0.5,0.4));
        String filename = "testing/circle10.txt";
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        StdDraw.clear();

        tree.draw();
        StdDraw.show();
    }
}