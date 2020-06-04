/*
@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)
 */
// Starter code for SP9

// Change to your netid
package sxv180069;

import java.util.NoSuchElementException;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    // add method: resize pq if needed
    public boolean add(T x) {
        if(pq.length <= size)
            resize();
        move(size, x);
        percolateUp(size);
        size++;
        return true;
    }

    public boolean offer(T x) {
        return add(x);
    }

    // throw exception if pq is empty
    public T remove() throws NoSuchElementException {
        T result = poll();
        if (result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    // return null if pq is empty
    public T poll() {
        if(isEmpty())
            return null;
        T min = min();
        move(0, pq[--size]);
        percolateDown(0);
        return min;
    }

    public T min() {
        return peek();
    }

    // return null if pq is empty
    public T peek() {
        return !isEmpty() ? (T)pq[0] : null;
    }

    int parent(int i) {
        return (i - 1) / 2;
    }

    int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * pq[index] may violate heap order with parent
     */
    void percolateUp(int index) {
        Comparable temp = pq[index];
        while(index > 0 && compare(pq[parent(index)], temp) > 0){
            move(index, pq[parent(index)]);
            index = parent(index);
        }
        move(index, temp);
    }

    /**
     * pq[index] may violate heap order with children
     */
    void percolateDown(int index) {
        T temp = (T)pq[index];
        int small = leftChild(index);
        while(small <= size - 1) {
            if(small < size-1 && compare(pq[small],pq[small + 1]) > 0){
                small = small + 1;
            }
            if(compare(temp, pq[small]) < 0)
                break;
            move(index, pq[small]);
            index = small;
            small = leftChild(index);
        }
        move(index, temp);
    }

    /**
     * use this whenever an element moved/stored in heap. Will be overridden by IndexedHeap
     */
    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /**
     * Create a heap.  Precondition: none.
     */
    void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    // Resize array to double the current size
    void resize() {
        Comparable[] temp = new Comparable[pq.length * 2];
        System.arraycopy(pq, 0, temp, 0, pq.length);
        pq = temp;
    }

    public interface Index {
        public void putIndex(int index);

        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        /**
         * Build a priority queue with a given array
         */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /**
         * restore heap order property after the priority of x has decreased
         */
        void decreaseKey(T x) {
            int current = x.getIndex();
            percolateUp(current);
        }

        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
            T cImg = (T)x;
            cImg.putIndex(i);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {100,50,25,12,6,3,2,1,0};
        BinaryHeap<Integer> h = new BinaryHeap<>(20);
        h.buildHeap();

        System.out.print("Before:");
        for (Integer x : arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = h.poll();
        }

        System.out.print("After :");
        for (Integer x : arr) {
            System.out.print(" " + x);
        }
        System.out.println();
    }
}
