/*
Implementation of MST algorithms: Boruvkas and Prim's
@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)
 */

package sxv180069;// Starter code for SP9

import sxv180069.Graph.Vertex;
import sxv180069.Graph.Edge;
import sxv180069.Graph.GraphAlgorithm;
import sxv180069.Graph.Factory;
import sxv180069.Graph.Timer;

import sxv180069.BinaryHeap.Index;
import sxv180069.BinaryHeap.IndexedHeap;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;
    public long wmst;
    List<Edge> mst;

    /*constructor*/
    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
        mst = new LinkedList<>();
        wmst = 0;
    }

    public static class MSTVertex implements Comparable<MSTVertex>, Factory, Index {
        int component;                           //component number to which the vertex belongs.
        boolean visited;                         //mark the vertex in count and label.

        MSTVertex parent;                   // parent of current MSTVertex
        int distance;                       // distance of current MSTVertex
        int index;                              // MSTVertex in IndexedHeap
        Vertex vertex;                            //corresponding vertex in original graph.

        /*
        MSTVertex Constructor
        @param u Vertex
         */
        MSTVertex(Vertex u) {
            if(u != null)
                component = u.getIndex();           //initialize component number for Boruvka's.

            parent = null;
            distance = Integer.MAX_VALUE;           // Infinity
            visited = false;
            vertex = u;                             //store the original vertex.
        }
        /*
        MSTVertex constructor for Prims
        @param u instance of MSTVertex
         */
        MSTVertex(MSTVertex u) {                    // for prim2
            parent = u.parent;
            distance = u.distance;
            visited = u.visited;
            vertex = u.vertex;
            index = u.index;

        }

        /*
        MSTVertex make function
        @param u instance of Vertex
         */
        public MSTVertex make(Vertex u) {
            return new MSTVertex(u);
        }

        /*
        Assign index
        @param indx index to be assigned
         */
        public void putIndex(int indx) {
            index = indx;
        }

        /*
        Returns current index value
        @param
        @return index
         */
        public int getIndex() {
            return index;
        }

        /*
        Compares current MSTVertex with given one
        @param other MSTVertex instnace
        @return comparison result (int)
         */
        public int compareTo(MSTVertex other) {
            //compare distance
            return Integer.compare(distance, other.distance);
        }
    }

    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        return wmst;
    }

    /*
    Boruvkas algorithm
    @return wmst weight of mst
     */
    public long boruvka() {
        algorithm = "Boruvka";
        Graph m = new Graph(this.g.size());
        MST F = new MST(m);

        int count = countAndLabel(F);

        while(count > 1){
            addSafeEdges(count, F);
            count = countAndLabel(F);
        }

        HashSet<Edge> uniqueEdges = new HashSet<>(mst);
        for(Edge edge: uniqueEdges){
            wmst += edge.getWeight();
        }

        return wmst;
    }

    /*
    Count and Label function for Borvuka's
    @param F instance of MST
     */
    public int countAndLabel(MST F){
        for(int i=0; i<F.g.size(); i++)
            this.get(F.g.getVertex(i+1)).visited  = false;

        int componentNumber = 0;

        for(int i=0; i<F.g.size(); i++){
            Vertex curr = F.g.getVertex(i+1);
            if(!this.get(curr).visited){
                this.get(curr).component = componentNumber++;
                dfs(F, i);
            }
        }

        return componentNumber;
    }

    /*
    implementation of DFS used in boruvka's
    @param F instance of MST
    @param pos integer value of position
     */
    private void dfs(MST F, int pos){
        Vertex curr = F.g.getVertex(pos+1);
        this.get(curr).visited = true;

        for(Edge edge: F.g.incident(curr)){
            Vertex neighbour = edge.otherEnd(curr);
            int idx = neighbour.getIndex();
            if(!this.get(neighbour).visited){
                this.get(neighbour).component = this.get(curr).component;
                dfs(F, idx);
            }
        }
    }

    /*
    Add Safe Edges function as per slides used in Boruvka's
    @param count integer value of count
    @param F instance of MST
     */
    public void addSafeEdges(int count, MST F){
        Edge[] safeEdges = new Edge[count];

        for(int i=0; i<count; i++)
            safeEdges[i] = null;

        for(Edge edge: this.g.getEdgeArray()){
            MSTVertex u = this.get(edge.from);
            MSTVertex v = this.get(edge.to);

            if(u.component != v.component) {
                if (safeEdges[u.component] == null || edge.compareTo(safeEdges[u.component]) < 0)
                    safeEdges[u.component] = edge;
                if (safeEdges[v.component] == null || edge.compareTo(safeEdges[v.component]) < 0)
                    safeEdges[v.component] = edge;
            }
        }

        for(int i=0; i<count; i++){
            if(safeEdges[i] != null){
                F.g.addEdge(safeEdges[i].from.getIndex(), safeEdges[i].to.getIndex(), safeEdges[i].weight);
                mst.add(safeEdges[i]);
            }

        }
    }

    /*
    Implementation of Prim's algorithm as per slides
    @param src source vertex
    @return wmst weight of mst
     */
    public long prim2(Vertex src) {
        algorithm = "Prim IndexedHeap";
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());

        for(Vertex u: g){
            get(u).visited = false;
            get(u).parent = null;
            get(u).distance = Integer.MAX_VALUE;
        }
        get(src).distance = 0;
        get(src).visited = true;
        wmst =0;
        for(Vertex u: g){
            q.add(get(u));
        }
        // follow algorithm from the lecture
        while(!q.isEmpty()){
            MSTVertex u = q.remove();
            u.visited = true;
            wmst += u.distance;

            for(Edge edge: g.incident(u.vertex)){
                Vertex vertex = edge.otherEnd(u.vertex);

                if(!get(vertex).visited && edge.getWeight() < get(vertex).distance){
                    get(vertex).distance = edge.getWeight();
                    get(vertex).parent = get(u.vertex);
                    q.decreaseKey(get(vertex));
                }
            }
        }

        return wmst;
    }

    public long prim1(Vertex s) {
        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>();
        return wmst;
    }

    /*
    Main function which runs the algorithms
    @param g Graph
    @param s Vertex
    @param choice algorithm choice of user
     */
    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch (choice) {
            case 0:
                m.boruvka();
                break;
            case 1:
                System.out.println("Prim #1 isn't implemented as the assignment suggested to implement #2.");
                break;
            case 2:
                m.prim2(s);
                break;
            case 3:
                System.out.println("Only Prim's and Boruvka's algorithm is implemented");
                break;
            default:
                break;
        }
        return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        int choice = 2;  // prim2
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) { choice = Integer.parseInt(args[1]); }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);
        Timer timer = new Timer();
        MST m = mst(g, s, choice);

        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }
}
