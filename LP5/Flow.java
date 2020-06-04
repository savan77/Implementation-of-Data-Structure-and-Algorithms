
/**
 * CS 6301.011: Implementation of Data Structures and Algorithms
 * Long Project LP5: Flow and Postman tour
 *
 * @author Shrey Shah (sxs190184)
 * @author Savan Visalpara (sxv180069)
 * @author Harshita Rastogi (hxr190001)
 * @author Tejas Gupta (txg180021)
 *
 */



// Starter code for max flow
package sxv180069;
import sxv180069.Graph;
import sxv180069.Graph.*;

import java.util.*;


public class Flow extends  GraphAlgorithm<Flow.FlowVertex>{
    // variables to be used in the algorithm
    Graph graph;
    Vertex source,sink;
    HashMap<Edge,Integer> capacity;
    HashMap<Edge,Integer> flow = new HashMap<>();
    LinkedList<Vertex> list = new LinkedList<>();
    Set<Vertex> minCutS = new HashSet<>();
    boolean mincuts = false;


    /* generic tuple to store new variables in Edge and Vertex
        For Edge, flow and residual flow
        For Vertex, height and excess.
        (Not used)
     */
    public class VertexTuple{
        public int a;    //i.e height
        public int b;   // i.e excess
        public VertexTuple(Integer a, Integer b){
            this.a = a;
            this.b = b;
        }
    }

    public static class FlowVertex implements Factory{
        //overloaded Vertex method to store
        // required information
        boolean seen;
        int height;
        int excess;
        // use isActive to check if a Vertex is in a list
        boolean isActive;
        Vertex org;

        public  FlowVertex(Vertex u){
            this.seen = false;
            this.isActive = false;
            this.height = 0;
            this.excess = 0;
            this.org = u;
        }
        public FlowVertex make(Vertex u) { return new FlowVertex(u); }
    }

    /*
    Constructor for Flow class.
    @param g instance of Graph
    @param s source Vertex
    @param t sink vertex
    @param capacity HashMap to store capacity of each edge
     */
    public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {
        super(g, new FlowVertex(null));
        this.graph = g;
        this.source = s;
        this.sink = t;
        this.capacity = capacity;
    }


    /*
    Main method which returns the Max flow using preflow push relabel algorithm
    @returns maxflow an int
     */
    public int preflowPush() {

        //initialize
        this.initialize();
        //loop
        while(list.size() != 0){
            //remove from list
            Vertex u = list.remove();
            get(u).isActive = false;
            //discharge
            this.discharge(u);
//            System.out.println("list: "+list);
            //relabel
            if(get(u).excess>0){
//                System.out.println("Running relabel");
                this.relabel(u);

            }
        }
//        System.out.println("max flow value");
        return get(sink).excess;
    }

    /*
    Initialize function to initialize flow values and excess values.
     */
    public void initialize(){
        for(Edge e: graph.getEdgeArray())
            flow.put(e,0);
        for(Vertex v:graph){
            get(v).excess = 0;
        }
        // find and set height
        bfs_height(sink);
        get(source).height = graph.size();
        for(Edge ed: graph.outEdges(source)){
            Vertex u = ed.otherEnd(source);
            flow.put(ed, capacity.get(ed));
            get(source).excess -= capacity.get(ed);
            get(u).excess += capacity.get(ed);
            get(u).isActive = true;
            list.add(u);
        }

    }

    /*
    BFS function to compute the height of vertices from sink vertex.
    @param t sink vertex

     */
    void bfs_height(Vertex t){
        graph.reverseGraph();
        Queue<Vertex> q = new LinkedList<>();
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        q.add(t);
        get(q.peek()).height = 0;
        visited.put(q.peek(),true);
        while(!q.isEmpty()){
            Vertex v = q.remove();
            if(!get(v).seen){
                get(v).seen = true;
//                System.out.println(v);
                for(Edge e : graph.incident(v)){
                    Vertex w = e.otherEnd(v);
                    if (!visited.containsKey(w) || !visited.get(w)) {
                        get(w).height = get(v).height + 1;
                        visited.put(w, true);
                        q.add(w);
                    }
                }
            }
        }
        graph.reverseGraph();
    }

    /*
    Function to recompute height
    @param u Vertex
     */
    public void relabel(Vertex u){
        int minHeight = Integer.MAX_VALUE;
        for(Edge e : graph.outEdges(u)){
            Vertex v = e.otherEnd(u);
            if(get(v).height < minHeight && capacity.get(e)-flow.get(e)>0)
                minHeight = get(v).height;
        }
        for(Edge e : graph.inEdges(u)){
            Vertex v = e.otherEnd(u);
            if(get(v).height < minHeight && flow.get(e)>0)
                minHeight = get(v).height;
        }
        get(u).height = minHeight + 1;
        get(u).isActive = true;
        list.add(u);
    }

    /*
    Function to find Admissible Edges
    @param g Graph
    @param u Vertex
    @returns admissibleArray an array of edges
     */
    public ArrayList<Edge> admissibleEdges(Graph g, Vertex u){
        ArrayList<Edge> admissibleArray = new ArrayList<>();
        int index = 0;
        for(Edge e: g.getEdgeArray())
        {
            // check for admissible edges
            // case 1: edges in the original graph
            if (e.from == u && get(e.from).height == get(e.to).height + 1){
                admissibleArray.add(e);
            }
            // case 2: reverse edges in the residual graph
            else if(flow.get(e) > 0 && (e.to ==u && get(e.to).height==get(e.from).height+1)) {
                admissibleArray.add(e);
            }
        }
        System.out.println("Admissible Edges: "+admissibleArray);
        return admissibleArray;
    }

    /*
   Discharge function for outedges
   @param u Vertex
    */
    public void dischargeOutEdges(Vertex u){
        // follow algorithm from the ppt
        for(Edge e : graph.outEdges(u)){
            Vertex v = e.otherEnd(u);
            if(get(u).height == get(v).height+1){
                // get min of u.excess of redidual capacity
                int delta = Math.min(get(u).excess,capacity.get(e)-flow.get(e));
                flow.put(e,flow.get(e)+delta);
                get(u).excess = get(u).excess - delta;
                get(v).excess = get(v).excess + delta;
                // As mentioned in the slide, dont search for v in the list
                // rather assign isActive variable and check here
                // don't add sink vertex
                if (get(v).excess > 0 && v != this.sink && get(v).isActive==false){
                    get(v).isActive=true;
                    list.add(v);
                }
                if(get(u).excess == 0)
                    return;
            }
        }
    }

    /*
    Discharge function for InEdges
    Additional cond: Consider edges with flow > 0
    @param u Vertex
     */
    public void dischargeInEdges(Vertex u){
        for(Edge e : graph.inEdges(u)){
            Vertex v = e.otherEnd(u);
            if((get(u).height == get(v).height + 1) && flow.get(e) > 0){
                int delta = Math.min(get(u).excess,flow.get(e));
                flow.put(e,flow.get(e)-delta);
                get(u).excess = get(u).excess - delta;
                get(v).excess = get(v).excess + delta;
                if (get(v).excess > 0 && v != this.sink && get(v).isActive == false){
                    get(v).isActive = true;
                    list.add(v);
                }
                if(get(u).excess == 0)
                    return;
            }
        }
    }


    /*
    Discharge function to compute excess values.
    @param u Vertex
     */
    public  void discharge(Vertex u){
        dischargeOutEdges(u);
        dischargeInEdges(u);
    }



    /* flow going through edge e
    @param e Edge
     */
    public int flow(Edge e) {
        return flow.get(e);
    }

    /* capacity of edge e
    @param e Edge
     */
    public int capacity(Edge e) {
        return capacity.get(e);
    }

    /* After maxflow has been computed, this method can be called to
        get the "S"-side of the min-cut found by the algorithm
     */
    public Set<Vertex> minCutS() {
        Queue<Vertex> q = new LinkedList<>();
        q.add(source);
        minCutS.add(source);
        while(!q.isEmpty()){
            Vertex v = q.remove();
            if(get(v).seen!= true){
                get(v).seen = true;
                for(Edge e: graph.outEdges(v)){
                    Vertex u = e.otherEnd(v);
                    if(capacity.get(e)-flow.get(e)>0)
                        minCutS.add(u);
                    q.add(u);
                }
            }
        }
        mincuts = true;
        return minCutS;
    }
    /* After maxflow has been computed, this method can be called to
       get the "T"-side of the min-cut found by the algorithm
    */
    public Set<Vertex> minCutT() {
        Set<Vertex> minCutT = new HashSet<>();
        if(mincuts == true){
            for(Vertex v : graph){
                if(!minCutS.contains(v))
                    minCutT.add(v);
            }
            return minCutT;
        }
        else
            return null;

    }
}