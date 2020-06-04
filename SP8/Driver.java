/*
Drive file to run given algorithms and measure time.
@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)
 */

package sxr170016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Driver{

    static CuckooHashing<Integer> ch = new CuckooHashing<>();
    static Set<Integer> hs = new HashSet<>();

    /**
     * @param args - a list of command line arguments
     * @return None (prints running time and other details)
     */
    public static void main(String[] args) throws FileNotFoundException {
        // read input from test file
        Scanner scan;
        File file = new File(args[0]);
        scan = new Scanner(file);

        String op;
        int operand;
        long modValue = 1000000009;
        long result = 0;
        boolean succeed = false;
        int choice = 0;

        // check if user has indicated which method to use
        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }

        Timer timer = new Timer();
        while (!((op = scan.next()).equals("End"))) {

            switch (op) {
                case "Add": {
                    operand = scan.nextInt();

                    if (choice == 1) {
                        ch.add(operand);
                    } else {
                        succeed = hs.add(operand);
                    }

                    if(succeed) {
                        result = (result + 1) % modValue;
                    }

                    break;
                }
                case "Remove": {
                    operand = scan.nextInt();

                    if (choice == 1) {
                        succeed = ch.remove(operand);
                    } else {
                        succeed = hs.remove(operand);
                    }

                    if(succeed) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Contains":{
                    operand = scan.nextInt();
                    if (choice == 1) {
                        succeed = ch.contains(operand);
                    } else {
                        succeed = hs.contains(operand);
                    }
                    if(succeed) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                default:
                    break;
            }
        }
        if (choice == 1) {
            System.out.println("Cuckoo Hashing:");
            System.out.println("Result: " + result);
            System.out.println("Size: " + ch.tableSize());

        } else {
            System.out.println("Java Hash Set");
            System.out.println("Result: " + result);
            System.out.println("Size: " + hs.size());
        }
        System.out.println(timer.end());
    }
}
