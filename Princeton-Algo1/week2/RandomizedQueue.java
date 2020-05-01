import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (N == s.length) resize(s.length * 2);
        s[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (N == 0) throw new java.util.NoSuchElementException();
        if (N == s.length/4) resize(s.length/2);
        int rand = StdRandom.uniform(N);
        Item item = s[rand];
        s[rand] = s[--N];
        s[N] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (N == 0) throw new java.util.NoSuchElementException();
        return s[StdRandom.uniform(N)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] copy;

        public ArrayIterator() {
            copy = (Item[]) new Object[N];
            for (int idx = 0; idx < N; idx++) copy[idx] = s[idx];
            StdRandom.shuffle(copy);
        }

        public boolean hasNext() { return i < N; }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return copy[i++];
        }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
        }
        for (Integer item : queue) {
            StdOut.print(item + " ");
        }
        StdOut.println();
        
        StdOut.println("The queue is empty: " + queue.isEmpty());
        StdOut.println("Queue size is: " + queue.size());
        StdOut.println("One sample is: " + queue.sample());
        StdOut.println("One sample is: " + queue.sample());
        StdOut.println("One sample is: " + queue.sample());

        StdOut.println("Remove item: " + queue.dequeue());
        StdOut.println("Remove item: " + queue.dequeue());
        StdOut.println("Remove item: " + queue.dequeue());
    }

}