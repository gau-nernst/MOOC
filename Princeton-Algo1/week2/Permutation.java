import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k == 0) return;
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            if (queue.size() == k) queue.dequeue();
            queue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) StdOut.println(queue.dequeue());
    }
 }