/*
Utility script to generate test cases for this short project.
@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)
 */
package sxv180069;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.lang.*;
import java.util.Random;

public class GenerateCases {
    /*
    Main function which generates test cases and writes into a text file.
     */
    public static void main (String[] args){
        if (args.length != 2){
            throw new RuntimeException("Please pass all two choices");
        }

        try{
            String fname = "mst-" + args[0] + '-' + args[1] +".txt";
            FileWriter writer = new FileWriter(fname);
            int nodes = Integer.parseInt(args[0]);
            int edges = Integer.parseInt(args[1]);
            int maxWeight = 2000;
            int[][] map = new int[nodes][nodes];
            int countedges = 0;
            Random r = new Random();
            writer.write(nodes+"\t"+edges+"\n");

            int count = 0;
            // run two loops to generate random connected graph
            for (int x = 1; x <= nodes && count < edges; x++) {
                for (int y = x+1; y <= nodes && count < edges; y++) {
                    writer.write(x + "\t" + y + "\t" + Integer.toString(r.nextInt(maxWeight)+1) + "\n");
                    count++;
                }
            }

            writer.close();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
