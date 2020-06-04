/* Starter code for LP3 */

// Change this to netid of any member of team
package sxv180069;

/**
 * CS 6301.011. Implementation of data structures and algorithms
 * Long Project LP3: Skip List Implementation
 * @author Shrey Shah (sxs190184)
 * @author Harshita Rastogi
 * @author Savan Visalpara (sxv180069)
 * @author Tejas Gupta
 *
 */

import java.util.NoSuchElementException;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int maxLevel = 32;

    private Entry<T> head, tail;

    public int size,maxHeight;

    private Entry<T>[] pred; //used in find

    private int[] distanceTraversed;

    private Random random;

    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        private int height;

        int[] span;

        /*
        Constructor
        @param x element value
        @param lev
        @returns none
         */
        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            height = lev;

            span = new int[lev];
            // add more code if needed
        }

        public E getElement() {
            return element;
        }
    }

    // Constructor
    public SkipList() {
        head = new Entry<>(null,maxLevel+1);
        tail = new Entry<>(null,maxLevel+1);

        size = 0;
        maxHeight = 1;

        for(int i = 0; i<maxLevel+1; i++){
            head.next[i] = tail;
            head.span[i] = 1;
        }

        pred = new Entry[maxLevel+1];
        distanceTraversed = new int[maxLevel+1];

        random = new Random();

        tail.prev = head;
    }

    /**
     * Helper method to search for x.
     * Sets last[i] = node at which search came down from
     * level i to i-1
     * @param x the element to be searched
     */
    private void findPred(T x){
        Entry<T> p = head;

        distanceTraversed = new int[maxLevel+1];

        for(int i = 0; i<maxHeight;i++){
            int in = maxHeight-1-i;
            while((p.next[in] != null) && (p.next[in].element != null) && (((T) p.next[in].element).compareTo(x) < 0)){
                distanceTraversed[in] += p.span[in];
                p = p.next[in];
            }
            pred[in] = p;
        }
    }

    /**
     * Helper method - add(x)
     * Chooses a random level
     * @return return the level
     */
    public int chooseHeight(){
        int height = 1 + Integer.numberOfLeadingZeros(random.nextInt());
        height = Math.min(height,maxHeight+1);
        if(maxHeight<height)
            maxHeight = height;
        return height;
    }

    /**
     * Insert x in the Skip list. return true on successful insertion.
     * else return false.
     * @param x the element to be added (generic type T)
     * @return true on successful insertion, false otherwise
     */
    public boolean add(T x) {
        if(contains(x))
            return false;

        int i = 0;
        int height = chooseHeight();
        Entry<T> e = new Entry(x,height);

        int predPosition = 0, addPosition = 0;

        for(i =0;i<distanceTraversed.length;i++)
            predPosition += distanceTraversed[i];

        addPosition = predPosition + 1;

        for(i = 0;i<height;i++){
            if(pred[i]==null)
                break;

            e.next[i] = pred[i].next[i];
            pred[i].next[i] = e;

            e.span[i] = predPosition + pred[i].span[i] - addPosition + 1;
            pred[i].span[i] = addPosition - predPosition;

            predPosition = predPosition - distanceTraversed[i];
        }
        e.height = i;

        e.next[0].prev = e;
        e.prev = pred[0];

        while(i<pred.length){
            if(pred[i]==null)
                break;
            pred[i].span[i]++;
            i++;
        }
        size++;

        for(int j = 0;j<maxLevel+1;j++){
            if(head.next[j]==tail)
                head.span[j] = size+1;
        }

        return true;
    }

    /* Find smallest element that is greater or equal to x
    @param x element value
     */
    public T ceiling(T x) {
        return null;
    }

    /**
     * Does the Skip list contains x?
     * @param x the element to be searched
     * @return true when x is present, false if not
     */
    public boolean contains(T x) {
        if(x==null)
            return false;

        findPred(x);

        if(pred[0].next[0].element!=null && ((T)pred[0].next[0].element).compareTo(x) == 0)
            return true;

        return false;
    }

    // Return first element of list
    public T first() {
        return null;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        return null;
    }

    /**
     * Return element at index n of list.
     * First element is at index 0.
     * @param n the input index
     * @return the element at index n
     */
    public T get(int n) {
        return getLog(n);
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
        return null;
    }

    /**
     * Helper method - get():
     * RT: O(log n) algorithm expected time for get(n)
     * @param n the input index
     * @return the element at index n
     * @throws NoSuchElementException When n is invalid index
     */
    public T getLog(int n) {
        int position  = n + 1;

        if(n<0 || size-1<n)
            throw new NoSuchElementException();

        int traversedPos = 0;
        Entry<T> p = head;

        int i = maxLevel;

        boolean is_non_tail = false;

        while(!is_non_tail){
            if(p.next[i].element!=null)
                is_non_tail = true;
            else
                i--;
        }
        while(i>-1){
            while((p.span[i]+traversedPos)<position){
                traversedPos += p.span[i];
                p = p.next[i];
            }
            i--;
        }
        return (T) p.next[0].element;
    }

    /**
     * Is the list empty?
     * @return true when empty Skip list, otherwise false
     */
    public boolean isEmpty() {
        if(size()<1)
            return true;
        return false;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return null;
    }

    // Return last element of list
    public T last() {
        return null;
    }


    // Not a standard operation in skip lists.
    public void rebuild() {

    }

    /**
     * Removes x from the list, if present.
     * Removed element is returned.
     * @param x the element to be removed
     * @return removed element, if present, else null
     */
    public T remove(T x) {
        if(!contains(x))
            return null;

        Entry<T> e = pred[0].next[0];

        int i = 0;
        while(i<e.height){
            pred[i].next[i] = e.next[i];
            pred[i].span[i] = pred[i].span[i] + e.span[i] - 1;
            i++;
        }

        while (pred[i] != null){
            pred[i].span[i]--;
            i++;
        }

        for(i = 0; i<maxLevel+1;i++){
            if(head.next[i]==tail)
                head.span[i] = size + 1;
        }
        size--;

        return e.element;
    }

    /**
     * Return the number of elements in the list
     * @return the size of the list
     */
    public int size() {
        return this.size;
    }
}
