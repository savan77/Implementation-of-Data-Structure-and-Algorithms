/** Starter code for LP2
 *  @author rbk ver 1.0
 *  @author SA ver 1.1
 */

// change to your netid
package sxv180069;

import sxv180069.Graph.*;

import java.util.Iterator;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;


public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    Vertex start;
    List<Vertex> tour;

    // You need this if you want to store something at each node
    static class EulerVertex implements Factory {


        EulerVertex(Vertex u) {
            boolean visited ;
        }
        public EulerVertex make(Vertex u) { return new EulerVertex(u); }

    }

    // To do: function to find an Euler tour
    public Euler(Graph g, Vertex start) {
        super(g, new EulerVertex(null));
        this.start = start;

        tour = new LinkedList<>();
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */
    public boolean isEulerian() {

        DFS scc = DFS.stronglyConnectedComponents(g);
        if (scc.numberOfConnectedComponents !=1) {
            System.out.println("The graph is not strongly connected!");
            return false;
        }

        for(Vertex v : g) {
            if(v.outDegree() != v.inDegree())
            {
                System.out.println("Graph is not Eulerian as\n inDegree= "+ v.inDegree() +" outDegree= "+v.outDegree()+" at vertex "+ v.getIndex());
                return false;
            }
        }
        return true;

    }


    public List<Vertex> findEulerTour() {

        System.out.println(isEulerian());
        //	if(!isEulerian()) { return new List<Vertex>(); }
        //else {
        //for(Edge e: g.incident(u)) {
        //u.EulerVertex
        //}
        //}
        // Graph is Eulerian...find the tour and return tour
        return tour;
    }

//    public void dfs( Vertex at) {
//    	for(Edge e: g.incident(at)) {
//    		dfs(e);
//    	}
//    		
//    	//    	while(g.getVertex(at).outDegree() !=0) {
////    		int next = g.getVertex(at).getVertex(--((at).outDegree()));
////    	}
//    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 1) {
            in = new Scanner(System.in);
        } else {
            //String input= "5 5	2 1 1	1 4 1	4 5 1	1 3 1	3 2 1";
            //String input ="5 6	2 1 1	1 4 1	4 5 1	5 1 1	1 3 1	3 2 1"; //is not eulerian
            //String input ="5 5 1 2 1 2 3 1 3 1 1 1 4 1 4 5 1"; //is eulerian
            String input = "9 13 1 2 1 2 3 1 3 1 1 3 4 1 4 5 1 5 6 1 6 3 1 4 7 1 7 8 1 8 4 1 5 7 1 7 9 1 9 5 1";
            in = new Scanner(input);
        }
        int start = 1;
        if(args.length > 1) {
            start = Integer.parseInt(args[1]);
        }
        // output can be suppressed by passing 0 as third argument
        if(args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);
        Vertex startVertex = g.getVertex(start);
        Timer timer = new Timer();


        Euler euler = new Euler(g, startVertex);
        List<Vertex> tour = euler.findEulerTour();


        timer.end();
        if(VERBOSE > 0) {
            System.out.println("Output:");
            // print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)
            System.out.println();
        }
        System.out.println(timer);


    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}