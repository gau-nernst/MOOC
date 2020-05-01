import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Item[] s;
    private int tail = 1;
    private int head = 1;

    // construct an empty deque
    public Deque() {
        s = (Item[]) new Object[3];
    }

    private void resize() {
        int capacity = tail - head;
        if (capacity == 0) {
            s = (Item[]) new Object[3];
            head = tail = 1;
        } else {
            Item[] copy = (Item[]) new Object[capacity * 3];
            for (int i = 0; i < capacity; i++) 
                copy[capacity + i] = s[head + i];
            s = copy;
            head = capacity;
            tail = capacity * 2;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == tail;
    }

    // return the number of items on the deque
    public int size() {
        return tail - head;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (head == 0) resize();
        s[--head] = item;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (tail == s.length) resize();
        s[tail++] = item;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = s[head];
        s[head++] = null;
        int capacity = tail - head;
        if (capacity > 0 && capacity == s.length/6) resize();
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = s[--tail];
        s[tail] = null;
        int capacity = tail - head;
        if (capacity > 0 && capacity == s.length/6) resize();
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }
    private class ArrayIterator implements Iterator<Item> {
        private int i = head;
        public boolean hasNext()    { return i < tail; }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException(); 
            return s[i++]; 
        }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 100; i++) {
            deque.addFirst(i);
        }

        for (Integer item : deque) {
            StdOut.print(item + " ");
        }
        StdOut.println();

        StdOut.println("The deque is empty: " + deque.isEmpty());
        StdOut.println("Deque size is: " + deque.size());
        StdOut.println("First item: " + deque.removeFirst());
        StdOut.println("Last item: " + deque.removeLast());
    }

}