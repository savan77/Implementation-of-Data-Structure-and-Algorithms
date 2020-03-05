/**
 * @author Savan Amitbhai Visalpara (sxv180069)
 * @author Srikumar Ramaswamy (sxr170016)
 *
 * This class implements Binary Heap with required methods.
 */


package sxr170016;

import java.util.NoSuchElementException;
import java.util.Scanner;

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
        pq[size] = x;
        percolateUp(size);
        size++;
        return true;
    }

    // add element in priority queue
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
        pq[0] = pq[size-1];
        percolateDown(0);
        size--;
        return min;
    }

    public T min() {
        return peek();
    }

    // return null if pq is empty
    public T peek() {
        return !isEmpty() ? (T)pq[0] : null;
    }

    // return parent
    int parent(int i) {
        return (i - 1) / 2;
    }

    // return left child
    int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * pq[index] may violate heap order with parent
     */
    void percolateUp(int index) {
        Comparable temp = pq[index];
        while(index > 0 && compare(pq[parent(index)], temp) > 0){
            int parentIdx = parent(index);
            pq[index] = pq[parentIdx];
            index = parentIdx;
        }
        pq[index] = temp;
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
            pq[index] = pq[small];
            index = small;
            small = leftChild(index);
        }
        pq[index] = temp;
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
    // check if heap is empty
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
        }

        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
        }
    }

    public static void main(String[] args) {
        // initialize random array
        Integer[] arr = {1,7,6,9,8,4,6,0,2,3};
        BinaryHeap<Integer> h = new BinaryHeap<>(20);
        h.buildHeap();


        System.out.print("Before:");
        for (Integer x : arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

        Scanner in = new Scanner(System.in);
        System.out.println("1 : Offer Element");
        System.out.println("2 : Remove Element");
        System.out.println("3 : Peek Element");
        System.out.println("4 : Check if Heap is Empty");
        System.out.println("5 : Get Current Size");


        whileloop:
        while(in.hasNext()) {
            int com = in.nextInt();
            switch(com) {
                case 1:           // add new element to the heap
                    System.out.print("Enter new value: ");
                    int value = in.nextInt();
                    h.offer(Integer.valueOf(value));
                    System.out.println("Offer operation was successful!");
                    break;
                case 2:    // Remove Element
                    System.out.println("Removed Element: " + h.remove());
                    break;
                case 3:    // Peek Element
                    System.out.println("Peek Element: " + h.min());
                    break;
                case 4:     // check if heap is empty
                    System.out.println("Is Empty?: " + h.isEmpty());
                    break;
                case 5:    // Return current size of the heap
                    System.out.println("Current Size: " + h.size());
                    break;
                default:  // Exit loop
                    break whileloop;
            }
        }
        System.out.println("Final Heap: ");
        while (!h.isEmpty()) {
            System.out.print(" " + h.poll());
        }
        System.out.println();

    }
}


/**
  Sample Program Run

Before: 1 7 6 9 8 4 6 0 2 3
1 : Offer Element
2 : Remove Element
3 : Peek Element
4 : Check if Heap is Empty
5 : Get Current Size
1
Enter new value: 5
Offer operation was successful!
3
Peek Element: 0
4
Is Empty?: false
2
Removed Element: 0
2
Removed Element: 1
5
Current Size: 9

 **/