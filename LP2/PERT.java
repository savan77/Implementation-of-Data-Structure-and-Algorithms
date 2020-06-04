/* Starter code for PERT algorithm (LP2)
 * @author rbk
 */

// change package to your netid
package sxv180069;

import sxv180069.Graph.Vertex;
import sxv180069.Graph.Edge;
import sxv180069.Graph.GraphAlgorithm;
import sxv180069.Graph.Factory;

import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ListIterator;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {

    public static class PERTVertex implements Factory {
        int duration;
        int ec;
        int lc;
        int slack;

        public PERTVertex(Vertex u) {
            ec = 0;
            slack = 0;
            duration = 0;
            lc = 0;
        }
        public PERTVertex make(Vertex u) { return new PERTVertex(u); }
    }

    public PERT(Graph g) {
        super(g, new PERTVertex(null));
        Vertex s = g.getVertex(1);
        Vertex t = g.getVertex(g.size());

        int m = g.edgeSize();

        for(int i = 2; i < g.size(); i++){
            g.addEdge(s,g.getVertex(i),1,++m);
            g.addEdge(g.getVertex(i),t,1,++m);
        }
    }

    public void setDuration(Vertex u, int d) {
        get(u).duration = d;
    }

    public boolean pert() {
        List<Vertex> topologicalOrder = DFS.topologicalOrder1(g);
        if(topologicalOrder == null)
            return true;
        for(Vertex u : g)
            get(u).ec = 0;
        for(Vertex u : topologicalOrder){
            for(Edge e : g.incident(u)){
                Vertex v = e.otherEnd(u);
                if(get(u).ec + get(v).duration > get(v).ec)
                    get(v).ec = get(u).ec + get(v).duration;
            }
        }
        int maxTime = get(g.getVertex(g.size())).ec;
        for(Vertex u : g)
            get(u).lc = maxTime;
        ListIterator<Vertex> reverseTopological = topologicalOrder.listIterator(topologicalOrder.size());
        while(reverseTopological.hasPrevious()){
            Vertex u = reverseTopological.previous();
            for( Edge e : g.incident(u)){
                Vertex v = e.otherEnd(u);
                if(get(v).lc - get(v).duration < get(u).lc)
                    get(u).lc = get(v).lc - get(v).duration;
            }
            get(u).slack = get(u).lc - get(u).ec;
        }
        return false;
    }

    public int ec(Vertex u) {
        return get(u).ec;
    }

    public int lc(Vertex u) {
        return get(u).lc;
    }

    public int slack(Vertex u) {
        return get(u).slack;
    }

    public int criticalPath() {
        return get(g.getVertex(g.size())).ec;
    }

    public boolean critical(Vertex u) {
        if(get(u).slack > 0)
            return false;
        return true;
    }

    public int numCritical() {
        int len = 0;
        for(Vertex u : g){
            if(get(u).slack == 0)
                len++;
        }
        return len;
    }

    // setDuration(u, duration[u.getIndex()]);
    public static PERT pert(Graph g, int[] duration) {
        PERT pert = new PERT(g);
        for(Vertex u: g)
            pert.setDuration(u, duration[u.getIndex()]);
        if(pert.pert())
            return pert;
        else
            return null;
    }

    public static void main(String[] args) throws Exception {
        String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        PERT p = new PERT(g);
        for(Vertex u: g) {
            p.setDuration(u, in.nextInt());
        }
        // Run PERT algorithm.  Returns null if g is not a DAG
        if(p.pert()) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for(Vertex u: g) {
                System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
    }
}
