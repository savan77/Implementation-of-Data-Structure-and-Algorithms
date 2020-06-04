// Driver code for max flow
package sxv180069;
import sxv180069.Graph;
import sxv180069.Graph.Edge;
import sxv180069.Graph.Vertex;

import java.util.HashMap;


public class maxFlowDriver {
    static int VERBOSE = 0;

    public static void main(String[] args) {
        if (args.length > 0) {
            VERBOSE = Integer.parseInt(args[0]);
        }
        java.util.Scanner in = new java.util.Scanner(System.in);
        Graph g = Graph.readDirectedGraph(in);
        Timer timer = new Timer();
        int s = in.nextInt();
        int t = in.nextInt();
        HashMap<Edge, Integer> capacity = new HashMap<>();
        int[] arr = new int[1 + g.edgeSize()];
        for (int i = 1; i <= g.edgeSize(); i++) {
            arr[i] = 1;   // default capacity
        }
        while (in.hasNextInt()) {
            int i = in.nextInt();
            int cap = in.nextInt();
            arr[i] = cap;
        }
        for (Vertex u : g) {
            for (Edge e : g.outEdges(u)) {
                capacity.put(e, arr[e.getName()]);
            }
        }

        Flow f = new Flow(g, g.getVertex(s), g.getVertex(t), capacity);
        //f.setVerbose(VERBOSE);
        int value = f.preflowPush();


        System.out.println(value);

        if (VERBOSE > 0) {
            for (Vertex u : g) {
                System.out.print(u + " : ");
                for(Edge e: g.outEdges(u)) {
                    System.out.print(e + ":" + f.flow(e) + "/" + f.capacity(e) + " | ");
                }
                System.out.println();
            }
            System.out.println("Min cut: S = " + f.minCutS());
            System.out.println("Min cut: T = " + f.minCutT());
        }

        System.out.println(timer.end());
    }
}

