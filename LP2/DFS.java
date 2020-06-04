/** Starter code for SP5
 *  @author rbk
 */
/**
 * Shrey Shah(sxs190184) Deepanshu Sharma(dxs190018)
 */
// change to your netid
package sxv180069;

import sxv180069.Graph.Vertex;
import sxv180069.Graph.Edge;
import sxv180069.Graph.GraphAlgorithm;
import sxv180069.Graph.Factory;
import sxv180069.Graph.Timer;

import java.util.Iterator;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * The class represents the implementation of Depth first Search
 *
 */
public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    private LinkedList<Vertex> topOrder;
    private boolean isCyclic;
    private int topNum;
    public int numberOfConnectedComponents;

    public static class DFSVertex implements Factory {
        int cno;
        boolean visited;
        Vertex parent;
        int topRank;
        /**
         * @param u
         * converting vertex u to DFSVertex
         */
        public DFSVertex(Vertex u) {
            this.cno = 0;
            this.visited = false;
            this.parent = null;
            this.topRank = 0;
        }
        public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }

    /**
     * @param g
     * Initialing g with numberOfConnectedComponents and isCyclic
     */
    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        this.isCyclic = false;
        numberOfConnectedComponents = 0;
    }

    /**
     * @param g
     * @return DFS object with the objects ordered
     */
    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);
        return d;
    }

    /**
     * Dfs method to run depth first search
     */
    public void dfs(Iterable<Vertex> iterable){
        topNum = g.size();
        numberOfConnectedComponents = 0;
        for (Vertex u :g) {
            get(u).visited = false;
            get(u).parent = null;
            get(u).topRank = 0;
        }
        topOrder = new LinkedList<Vertex>();
        for (Vertex u : iterable){
            if(!get(u).visited){
                get(u).cno = ++numberOfConnectedComponents;
                DFS_visit(u);
            }
        }
    }
    /**
     * @param u
     * Visiting each node adjacent to Vertex u
     */
    void DFS_visit(Vertex u){
        get(u).visited = true;
        for(Edge e : g.outEdges(u)){
            Vertex v = e.otherEnd(u);
            if(!get(v).visited){
                get(v).parent = u;
                get(v).cno = get(u).cno;
                DFS_visit(v);
            }
            else{
                if(get(v).topRank==0)
                    isCyclic = true;
            }
        }
        get(u).topRank = topNum--;
        topOrder.addFirst(u);
    }

    /**
     * @return list of vertices in topological order
     */
    public List<Vertex> topologicalOrder1() {
        dfs(g);
        if(isCyclic) return null;
        return topOrder;
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        dfs(g);
        return numberOfConnectedComponents;
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

    /**
     * Method to find strongly connected components of a dirested graph
     * @param g-Graph
     * @return DFS
     */
    public static DFS stronglyConnectedComponents(Graph g){
        DFS d = new DFS(g);
        d.dfs(g);
        List<Vertex> finishList = d.topOrder;
        g.reverseGraph();
        d.dfs(finishList);
        g.reverseGraph();
        return d;
    }

    /**
     * Print the components of graph
     */
    public void printComponents(){
        System.out.println("Number of components: "+numberOfConnectedComponents);
        for(Vertex u: topOrder)
            System.out.println(u+"   "+cno(u));
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string = "11 17   1 11 0   2 7 0   2 3 0   3 10 0   4 9 0   4 1 0   5 4 0   5 8 0   5 7 0   6 3 0   7 8 0   8 2 0   9 11 0   10 6 0   11 3 0   11 6 0   11 4 0 0";
        // String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";
        // String string = "7 7   1 2 0   1 3 0   2 4 0   3 4 0   4 5 0   5 6 0   6 7 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        DFS scc = DFS.stronglyConnectedComponents(g);
        scc.printComponents();
    }
}