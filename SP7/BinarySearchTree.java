/**
 * @author
 * Srikumar Ramaswamy (sxr170016)
 * Savan Amitbhai Visalpara (sxv180069)
 *
 * Binary search tree (starter code)
 **/

package sxr170016;

import java.util.*;

/*
Class for Binary Search Tree which implements and Iterable
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        /*
        Constructor for Entry
        @param x     value
        @param left  left node
        @param right right node
         */
        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root;
    int size;
    Stack<Entry<T>> stack;

    // constructor of the main class
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Check if x is in tree?
     * @param x node value
     */
    public boolean contains(T x) {
        Entry<T> t = find(x);
        if (t == null || !t.element.equals(x))
            return false;
        return true;
    }

    /**
     * Check if there is an element that is equal to x in the tree.
     * Return lement in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
        if (contains(x))
            return find(x).element;
        else
            return null;

    }

    /**
     * If tree contains a node with same key, replace element by x.
     * Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
        if (size == 0) {
            root = new Entry(x, null, null);
            size++;
            return true;
        } else {
            Entry<T> t = find(x);
            if (t.element.equals(x))
                return false;
            if (x.compareTo(t.element) < 0)
                t.left = new Entry(x, null, null);
            else
                t.right = new Entry(x, null, null);
            size++;
            return true;
        }
    }

    /**
     * @param x - element to be searched
     * @return - Entry containing the element.
     * returns null if the element cannot be found.
     */
    private Entry<T> find(T x) {
        stack = new Stack<Entry<T>>();
        stack.push(null);
        return find(root, x);
    }

    /**
     * @param t - Entry from where we need to start the find.
     * @param x - Element to be found.
     * @return
     */
    private Entry<T> find(Entry<T> t, T x) {
        if (t == null || t.element.equals(x))
            return t;

        while (true) {
            if (x.compareTo(t.element) < 0) {
                if (t.left == null)
                    break;
                stack.push(t);
                t = t.left;
            } else if (t.element.equals(x))
                break;
            else if (t.right == null)
                break;
            else {
                stack.push(t);
                t = t.right;
            }
        }
        return t;
    }

    /**
     * @param x element to be searched in the binary search tree.
     * Return x if found, otherwise return null
     */
    public T remove(T x) {
        if (size == 0)
            return null;
        Entry<T> t = find(x);
        if (t.element != x)
            return null;
        if (t.left == null || t.right == null)
            splice(t);
        else {
            stack.push(t);
            Entry<T> minRight = find(t.right, x);
            t.element = minRight.element;
            splice(minRight);
        }
        size--;
        return x;
    }

    /**
     * Helper method to remove an element from the binary search tree.
     * @param t element to be removed from the binary search tree.
     */
    private void splice(Entry<T> t) {
        Entry<T> parent = stack.peek();
        Entry<T> child = t.left == null ? t.right : t.left;
        if (parent == null)
            root = child;
        else if (t.equals(parent.left))
            parent.left = child;
        else
            parent.right = child;
    }

    /**
     * Find the element with minimum value from the binary search tree.
     * @return the element with minimum value.
     */
    public T min() {
        Entry<T> temp = root;
        if(temp == null)
            return null;
        while (temp.left != null) {
            temp = temp.left;
        }
        return temp.element;
    }

    /**
     * Find the element with maximum value from the binary search tree.
     * @return the element with maximum value.
     */
    public T max() {
        Entry<T> temp = root;
        if(temp == null)
            return null;
        while(temp.right != null) {
            temp = temp.right;
        }
        return temp.element;
    }

    /**
     * Store the elements of the binary search tree in an array.
     * @return the array containing the elements of the BST.
     */
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        List<Comparable> lst = new ArrayList<>();
        inOrder(root, lst);
        for(int i=0; i<lst.size(); i++)
            arr[i] = lst.get(i);
        return arr;
    }

    /**
     * Helper method which performs in-order traversal on the BST and stores the element in a list.
     * @param t root of the BST.
     * @param lst list in which the elements of the BST are appended.
     */
    private void inOrder(Entry<T> t, List<Comparable> lst) {
        if(t == null)
            return ;
        inOrder(t.left, lst);
        lst.add(t.element);
        inOrder(t.right, lst);
    }

    // Start of Optional problem 2

    /**
     * Optional problem 2: Iterate elements in sorted order of keys
     * Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
        return null;
    }

    // Optional problem 2.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem 2.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem 2.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem 2.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

    // End of Optional problem 2


    // main method
    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if (x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for (int i = 0; i < t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();

                System.out.println("");
                System.out.println();
                return;
            }
        }

    }

    /*
    Prints tree nodes.
     */
    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    /* Inorder traversal of tree
    @param node object of Entry
     */
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10

*/
