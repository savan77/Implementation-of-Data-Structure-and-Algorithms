/*
        * CS 6301.011. Implementation of data structures and algorithms
        * Long Project LP3: Skip List & RBT Implementation
        * @author Savan Visalpara (sxv180069)
        * @author Shrey Shah (sxs190184)
        * @author Harshita Rastogi (hxr190001)
        * @author Tejas Markande Gupta (txg180021)
        */

package sxv180069;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;


    /***
     * This class represents an Entry for the RBT
     * It is of generic type and extends Entry from BinarySearchTree class
     */
    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color; //true is RED and false is BLACK

        /***
         * Constructor
         * @param x:		element to be stored in the RBT
         * @param left:		entry to the left of the RBT
         * @param right:	entry to the right of the RBT
         */
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        /**
         * Checks if the node color is Red
         * @return {@code true}:	node is RED
         * 		   {@code true}:	node is BLACK
         */
        boolean isRed() {
            return color == RED;
        }

        /**
         * Checks if the node color is Red
         * @return {@code true}:	node is BLACK
         * 		   {@code true}:	node is RED
         */
        boolean isBlack() {
            return color == BLACK;
        }

        /*
         * Overriding the toString method of the Object class.
         * prints the elements and it's color
         */
        public String toString(){
            if(element!=null)
                return (color? "RED ":"BLACK " )+  element.toString();
            else
                return "NIL";
        }

    }

    // NIL node which forms the leaf node in RBT
    private Entry<T> NIL;

    /**
     * Class Constructor
     */
    public RedBlackTree() {
        super();
        NIL = new Entry(null,null,null);
        NIL.color = BLACK;
    }

    /***
     * Outputs the parent of the inputed node
     * @param x Node whose parent is to found
     * @return {@codesparent} if parent exist
     * 		   {@code null} if inputed node is the root node
     */
    private Entry<T> getParent(Entry<T> x){
        return (Entry<T>)super.parent(x);
    }

    /***
     * Outputs the Uncle(child of grandparent of the inputed node other than immediate parent) of the inputed node
     * @param x Node whose uncle is to found
     * @return uncle of inputed node
     */
    private Entry<T> uncle(Entry<T> x){
        return (Entry<T>)super.uncle(x);
    }

    /***
     * Outputs the sibling(another child of immediate parent) of the inputed node
     * @param x Node whose sibling is to be found
     * @return Sibling of the inputed node
     */
    private Entry<T> getSibling(Entry<T> x){
        if(x == root)
            return null;
        if(x.isLeftChild())
        {
            return (Entry<T>)parent(x).right;
        }
        else
            return (Entry<T>)parent(x).left;
    }

    /***
     * Adds element to the tree
     * @param x element to be added.
     * @return {@code	true}	if x is added to the tree
     * 		   {@code	false}	if x already exists
     */
    public boolean add(T x){
        Entry<T> cursor = new Entry<T>(x,NIL,NIL);
        if(!super.add(cursor))
            return false;
        Entry<T> parent = getParent(cursor);
        Entry<T> uncle, grandParent;
        while(cursor!=root && size > 3 && parent.color != BLACK)
        {
            if(parent.isLeftChild())
            {
                uncle = uncle(cursor);
                if(uncle.isRed())
                {
                    parent.color = uncle.color = BLACK;
                    cursor = getParent(parent);
                    cursor.color = RED;
                }
                else
                {
                    if(cursor.isRightChild())
                    {
                        cursor = getParent(cursor);
                        leftRotate(cursor);
                    }
                    parent = getParent(cursor);
                    parent.color = BLACK;
                    grandParent = getParent(parent);
                    if(grandParent != null){
                        grandParent.color = RED;
                        rightRotate(grandParent);
                    }
                }
            }
            else
            {
                uncle = uncle(cursor);
                if(uncle.isRed())
                {
                    parent.color = uncle.color = BLACK;
                    cursor =getParent(parent);
                    cursor.color = RED;
                }
                else
                {
                    if(cursor.isLeftChild())
                    {
                        cursor = getParent(cursor);
                        rightRotate(cursor);
                    }
                    parent = getParent(cursor);
                    parent.color = BLACK;
                    grandParent = getParent(parent);
                    if(grandParent != null){
                        grandParent.color = RED;
                        leftRotate(grandParent);
                    }
                }
            }
            parent = getParent(cursor);
        }
        ((Entry<T>)root).color = BLACK;
        return true;
    }

    /***
     * Verifies the properties of Red Black Tree
     * @return {@code	true}if all properties are satisfied
     * 		   {@code	false}if any property is not satisfied
     */
    public boolean verifyRBT(){
        boolean rootp,  // The root is black
                leafp,  // All leaf nodes are black nil nodes
                redp,  // The parent of a red node is black or red parent has black children
                depthp;  // The number of black nodes on each path from the root to a leaf node is the same
        HashSet<Entry<T>> leaves = new HashSet<>();

        rootp = ((Entry<T>)(root)).isBlack();

        Queue<Entry<T>> bfs = new LinkedList<>();
        redp = true; //Initialize

        bfs.add((Entry<T>)root);
        while(!bfs.isEmpty() && redp){
            Entry<T> rt =  bfs.remove();
            if(rt.isRed()) {
                redp = ((Entry<T>)rt.left).isBlack();
                redp = redp && ((Entry<T>)rt.right).isBlack();
            }

            if(rt.left != null)
            {
                bfs.add((Entry<T>)rt.left);
            }
            if(rt.right != null)
            {
                bfs.add((Entry<T>)rt.right);
            }
            if(rt.left == null && rt.right == null){
                leaves.add(rt);
            }
        }
        leafp = leaves.size() == 1;

        depthp = getBlackHeight((Entry<T>)root) != -1;

        return rootp && redp && redp && depthp;
    }

    /***
     * Outputs the height of the tree from a given node
     * @param Node from which the height is to be calculated
     * @return height from the inputed node
     */
    int getBlackHeight(Entry<T> root)
    {
        if (root == null)
            return 1;

        int BlackHeightLeft = getBlackHeight((Entry<T>)root.left);
        if (BlackHeightLeft == 0)
            return BlackHeightLeft;

        int BlackHeightRight = getBlackHeight((Entry<T>)root.right);
        if (BlackHeightRight == 0)
            return BlackHeightRight;

        if (BlackHeightLeft != BlackHeightRight)
            return 0;
        else
            return BlackHeightLeft + (root.isBlack() ? 1 : 0);
    }


    /***
     * This function copies element value from source node to destination node
     * @param dest destination node
     * @param src source node
     */
    protected void copy(BinarySearchTree.Entry<T> dest, BinarySearchTree.Entry<T> src){
        super.copy(dest,src);
        ((Entry<T>)dest).color = ((Entry<T>)src).color;
    }


    /***
     * Removes the entry x from the tree
     * @param x entry to be removed
     * @return {@code	element}if x exists
     * 		   {@code	null}if x doesn't exist
     */
    public T remove(T x)
    {
        if(size == 0) return null;
        Entry<T> t = (Entry<T>)find(x);
        if(t.element.compareTo(x) != 0) return null;
        super.remove(t);
        boolean splicedEntryColor = ((Entry<T>)splicedEntry).color;
        Entry<T> cursor = (Entry<T>)splicedChild;

        if(splicedEntryColor == BLACK) {
            fixUp(cursor);
        }
        return x;
    }

    /**
     * Maintains the RBT properties after deletion
     * @param cursor the child of the node spliced during delete
     */
    private void fixUp(Entry<T> cursor){
        Entry<T> sibling, parent, sibling_left, sibling_right;
        while(cursor != root && cursor.isBlack()){
            if(cursor.isLeftChild())
            {
                sibling = getSibling(cursor);
                // case 1
                if(sibling.isRed())
                {
                    sibling.color = BLACK;
                    parent = getParent(cursor);
                    parent.color = RED;
                    leftRotate(parent);
                    sibling = getSibling(cursor);
                }
                if(sibling == NIL) break;
                sibling_left = (Entry<T>)sibling.left;
                sibling_right = (Entry<T>)sibling.right;

                // case 1
                if(sibling_left.isBlack() && sibling_right.isBlack())
                {
                    sibling.color = RED;
                    cursor = getParent(cursor);
                }
                else
                {
                    // case 3
                    if(sibling_left != NIL && sibling_right.isBlack())
                    {
                        sibling_left.color = BLACK;
                        sibling.color = RED;
                        rightRotate(sibling);
                        sibling = getSibling(cursor);
                    }

                    if(sibling == NIL) break;
                    sibling_left = (Entry<T>)sibling.left;
                    sibling_right = (Entry<T>)sibling.right;

                    // case 4
                    sibling_right.color = BLACK;
                    parent = getParent(cursor);
                    sibling.color = parent.color;
                    parent.color = BLACK;
                    leftRotate(parent);
                    cursor = (Entry<T>)root;
                }
            }
            else
            {
                sibling = getSibling(cursor);

                if(sibling.isRed()) // Case 1
                {
                    sibling.color = BLACK;
                    parent = getParent(cursor);
                    parent.color = RED;
                    rightRotate(parent);
                    sibling = getSibling(cursor);
                }
                if(sibling == NIL) break;
                sibling_left = (Entry<T>)sibling.left;
                sibling_right = (Entry<T>)sibling.right;

                if(sibling_left.isBlack() && sibling_right.isBlack()) //  Case 2
                {
                    sibling.color = RED;
                    cursor = getParent(cursor);
                }
                else
                {
                    if(sibling_right != NIL && sibling_left.isBlack())  // Case 3
                    {
                        sibling_right.color = BLACK;
                        sibling.color = RED;
                        leftRotate(sibling);
                        sibling = getSibling(cursor);
                    }
                    if(sibling == NIL) break;
                    sibling_left = (Entry<T>)sibling.left;
                    sibling_right = (Entry<T>)sibling.right;

                    sibling_left.color = BLACK; // Case 4
                    parent = getParent(cursor);
                    sibling.color = parent.color;
                    parent.color = BLACK;
                    rightRotate(parent);
                    cursor = (Entry<T>)root;
                }
            }
        }
    }



}