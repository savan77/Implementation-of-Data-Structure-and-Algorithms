/*
Class that finds odd length cycle in a graph.
 */

package sxv180069;

import sxv180069.BFSOO;
import sxv180069.Graph.Vertex;
import sxv180069.Graph.Edge;

import java.io.File;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;

/*
Class that detects odd-length cycle in a graph.
 */
public class OddCycle {
    /*
    * @param g input graph
    * @returns odd length cycle (list of vertex)
    **/
    public static List<Vertex> oddCycle(Graph g) {
        Vertex[] vertexArray = g.getVertexArray();
        BFSOO bfsnode = null;
        for (Vertex src: vertexArray) {     //for each vertex
            if (bfsnode == null || !bfsnode.getSeen(src)) {
                bfsnode = BFSOO.breadthFirstSearch(g, src);   //apply BFS and find next vertex
                for (Vertex a: vertexArray) {
                    if (bfsnode.getSeen(a)) {
                        for (Edge edge: g.outEdges(a)) {
                            Vertex u = edge.otherEnd(a);
                            // find the distance
                            if (bfsnode.getDistance(a) == bfsnode.getDistance(u)) {
                                List<Vertex> Ustart = new ArrayList<>();
                                List<Vertex> Vstart = new ArrayList<>();
                                // find vertexes from u and v
                                while (a != u) {
                                    Ustart.add(a);
                                    Vstart.add(0, u);
                                    a = bfsnode.getParent(a);
                                    u = bfsnode.getParent(u);
                                }
                                //add node to Ustart and Vstart
                                Ustart.add(a);
                                Vstart.add(0, u);
                                Vstart.addAll(Ustart);
                                return Vstart;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string = "8 11   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 -7   6 7 -1   7 6 -1   7 8 1   8 6 1   3 2 1 3";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
        // Read graph from input
        Graph g = Graph.readGraph(in, false);
        g.printGraph(false);
        int s = in.nextInt();

        // Call breadth-first search to find the odd length cycle
        List<Vertex> oddLength = oddCycle(g);
        System.out.println(oddLength!=null?oddLength:"The graph does not have odd length cycles.");
    }
}

/*
Sample Run
____________________________________________
Graph: n: 8, m: 11, directed: false, Edge weights: false
1 :  (1,2) (1,3) (5,1)
2 :  (1,2) (2,4) (3,2)
3 :  (1,3) (3,4) (3,2)
4 :  (2,4) (3,4) (4,5)
5 :  (4,5) (5,1)
6 :  (6,7) (7,6) (8,6)
7 :  (6,7) (7,6) (7,8)
8 :  (7,8) (8,6)
______________________________________________
[1, 3, 2, 1]
 */