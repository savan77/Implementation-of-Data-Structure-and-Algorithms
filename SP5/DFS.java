/**
* Starter code for SP5
*
* @author Savan Amitbhai Visalpara (sxv180069)
* @author Srikumar Ramaswamy (sxr170016)
*/

package sxr170016;

import sxr170016.Graph.Vertex;
import sxr170016.Graph.Edge;
import sxr170016.Graph.GraphAlgorithm;
import sxr170016.Graph.Factory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

    public LinkedList<Vertex> topList;               //stores the topological order.
    private int numSize;                        // used to store size of the graph
    public static boolean cyclic;                // bool var to store whether its cyclic

    public static class DFSVertex implements Factory {
        int cno;
        public boolean visited;     // store if the node is visited
        public Vertex parent;      // store parent of the node
        public int top;           // store ref to back edge

        /**
        * @param u Vertex object
        */
        public DFSVertex(Vertex u) {  // convert Vertex to DFSVertex
            this.visited = false;
            this.parent = null;
            this.top = 0;

        }



        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    /**
    * @param g Graph
    */
    public DFS(Graph g) {   //constructor with initialization
        super(g, new DFSVertex(null));
        cyclic = false;
    }

    /**
    * @param g Graph obj
    * Runs depth first search to find topological ordering.
    */

    public static List<Vertex> topologicalOrdering(Graph g){
        DFS d = new DFS(g);
        return d.topologicalOrder1();     
    }

    /** Depth First Search method.
    */

    public void depthFirstSearch() {
        numSize = g.size();
        for(Vertex v: g) {
            get(v).visited = false;
            get(v).parent = null;             //unmark
            get(v).top = 0;
        }
        topList = new LinkedList<Vertex>();
        for(Vertex v : g) {
            if(!get(v).visited) {
                depthFirstSearch(v);
            }
        }
    }

    /**
    * @param u Vertex obj
    * Visits each node adjacent to a given node-v.
    */
    public void depthFirstSearch(Vertex v) {
        this.get(v).visited  = true;
        for(Edge edge: this.g.incident(v)) {
            Vertex w = edge.otherEnd(v);
            if(!this.get(w).visited) {
                get(w).parent = v;
                depthFirstSearch(w);
            }
            else{   
                if (get(w).top == 0){   // found cycle?
                    cyclic = true;
                }
            }
        }
        get(v).top = numSize--;
        topList.addFirst(v);
    }

    // function to find topological order
    public List<Vertex> topologicalOrder1() {
        depthFirstSearch();
        if(cyclic){      // check if contains cycle in graph
            return null;
        }
        return topList;
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        return 0;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = new DFS(g);
        return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string = "7 7   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1  5 6 1  6 7 1   0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readGraph(in, true);
        g.printGraph(false);

        DFS dfs = new DFS(g);
        List<Vertex> res = dfs.topologicalOrder1();
        if(res == null){
            System.out.print("Graph contains a cycle. No topological ordering is possible. (null returned)");
        	System.exit(0); //exit the program
        }
        System.out.println("Topological Order: ");
        for(Vertex vertex: res) {
            System.out.print(vertex + " ");
        }
    }
}



/* Sample Run

Graph: n: 7, m: 7, directed: true, Edge weights: false
1 :  (1,2) (1,3)
2 :  (2,4)
3 :  (3,4)
4 :  (4,5)
5 :  (5,6)
6 :  (6,7)
7 :
______________________________________________
Topological Order:
1 3 2 4 5 6 7



______________________________________________
Graph: n: 7, m: 8, directed: true, Edge weights: false
1 :  (1,2) (1,3)
2 :  (2,4)
3 :  (3,4)
4 :  (4,5)
5 :  (5,1)
6 :  (6,7)
7 :  (7,6)
______________________________________________
Graph contains a cycle. No topological ordering is possible. (null returned)

*/