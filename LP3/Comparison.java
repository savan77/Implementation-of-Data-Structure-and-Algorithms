
/**
 * CS 6301.011. Implementation of data structures and algorithms
 * Long Project LP3: Skip List & RBT Implementation
 * @author Savan Visalpara (sxv180069)
 * @author Shrey Shah (sxs190184)
 * @author Harshita Rastogi (hxr190001)
 * @author Tejas Markande Gupta (txg180021)
 */

package sxv180069;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;
import java.lang.Math;

//Driver program for comparison of red black tree and TreeSet.

public class Comparison {
    /*
    Main function which runs given algorithm on choosen input
    @param args
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = null;
        long[] randomNumbers = null;


        boolean random = false; //use random generator
        if (args.length > 0) {
            if (args[0].endsWith("txt")) {
                File file = new File(args[0]);
                sc = new Scanner(file);
            }
            else{//generate random numbers
                random = true;
                randomNumbers = new long[Integer.parseInt(args[0])];
                for(int i=0; i < Long.parseLong(args[0]); i++){
                    randomNumbers[i] = (long)(Math.random()*((100000-100)+1))+100;
                }
            }
        }
        else{
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        boolean succeed = false;
        int choice = 0;

        TreeSet<Long> treeSet = new TreeSet<>();
        RedBlackTree<Long> redBlackTree = new RedBlackTree<>();
        SkipList<Long> skipList = new SkipList<>();

        // check if user has indicated which method to use
        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);   // 1 for treeset or 2 for SkipList or RBT by default
        }

        // Initialize the timer
        Timer timer = new Timer();
        if (random){    //run on random numbers
            for (long i:randomNumbers) {
                if (choice ==1) {       // treeset
                    treeSet.add(i);
                }
                else if (choice ==2){  // skiplist
                    skipList.add(i);
                }
                else{  //red black tree
                    redBlackTree.add(i);
                }
            }
            for (long i:randomNumbers) {
                if (choice ==1) {
                    treeSet.contains(i);
                }
                else if (choice ==2){
                    skipList.contains(i);
                }
                else{
                    redBlackTree.contains(i);
                }
            }
            for (long i:randomNumbers) {
                if (choice ==1) {
                    treeSet.remove(i);
                }
                else if (choice ==2){
                    skipList.remove(i);
                }
                else{
                    redBlackTree.remove(i);
                }
            }
            timer.end();
            if (choice == 1){
                System.out.println("Tree Set");
            }
            else if (choice == 2){
                System.out.println("Skip List");
            }
            else{
                System.out.println("Red Black Tree");
            }
            System.out.println(timer);
            return;
        }
        else {
            // loop though input file
            while (!((operation = sc.next()).equals("End"))) {
                switch (operation) {
                    case "Add": {
                        operand = sc.nextLong();
                        if (choice == 1) {   // treeset
                            succeed = treeSet.add(operand);
                        }
                        else if(choice ==2){
                            succeed = skipList.add(operand);
                        } else {  // red-black tree
                            succeed = redBlackTree.add(operand);
                        }
                        if (succeed) {
                            result = (result + 1) % modValue;
                        }
                        break;
                    }
                    case "Remove": {
                        operand = sc.nextLong();
                        if (choice == 1) {     // treeset
                            if (treeSet.remove(operand)) {
                                result = (result + 1) % modValue;
                            }
                        }
                        else if (choice ==2){
                            if (skipList.remove(operand) != null){
                                result = (result + 1) % modValue;
                            }
                        } else {                         // red black tree
                            if (redBlackTree.remove(operand) != null) {
                                result = (result + 1) % modValue;
                            }
                        }

                        break;
                    }
                    case "Contains": {
                        operand = sc.nextLong();
                        if (choice == 1) {     // treeset
                            succeed = treeSet.contains(operand);
                        }
                        else if(choice == 2){
                            succeed = skipList.contains(operand);
                        }
                        else {                 // red black tree
                            succeed = redBlackTree.contains(operand);
                        }
                        if (succeed) {
                            result = (result + 1) % modValue;
                        }
                        break;
                    }
                }
            }
        }

        // End Time
        timer.end();

        if (choice == 1) {
            System.out.println("Tree Set:");
            System.out.println("Result: " + result);
            System.out.println("Size: " + treeSet.size());

        } else if (choice == 2){
            System.out.println("SkipList");
            System.out.println("Result: " + result);
        }
        else {
            System.out.println("Red Black Tree");
            System.out.println("Result: " + result);
        }
        System.out.println(timer);
    }
}