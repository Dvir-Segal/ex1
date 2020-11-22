package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph g;
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     * @return
     */
    @Override
    public weighted_graph copy() throws Exception {
        weighted_graph copy = new WGraph_DS(g);
        return copy;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. The methode uses dikjstra methode
     * @return
     */
    @Override
    public boolean isConnected() {
        if (g == null || g.getV().size() < 2)
            return true;
        Iterator<node_info> iter = g.getV().iterator();
        node_info src = iter.next();
        //If dikjstra = true, we have isolated node and the graph isn't connected
        if (this.dikjstra(src.getKey()))
            return false;
        return true;
    }
    /**
     * returns the length of the shortest path between src to dest. The methode uses dikjstra methode,
     * by the tag's field.
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (g == null || !this.g.getV().contains(g.getNode(src)) || !this.g.getV().contains(g.getNode(dest)))
            return -1;
        this.dikjstra(src);
        return this.g.getNode(dest).getTag();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * The methode uses dikjstra methode, by the parent's field.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (g == null || src == dest)
            return null;
        this.dikjstra(src);
        if(this.g.getNode(src).getTag() == Double.POSITIVE_INFINITY || this.g.getNode(dest).getTag() == Double.POSITIVE_INFINITY)
            return null;
        LinkedList<node_info> list = new LinkedList<>();
        list.add(this.g.getNode(dest));
        node_info p = ((WGraph_DS.NodeInfo)this.g.getNode(dest)).getParent();
        while (p!=null){
            list.add(p);
            p = ((WGraph_DS.NodeInfo)p).getParent();
        }
        Collections.reverse(list);
        return list;
    }
    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try{
            FileOutputStream savedGraph = new FileOutputStream(file);
            ObjectOutputStream operator = new ObjectOutputStream(savedGraph);
            operator.writeObject(this.getGraph());
            operator.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name.
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream savedGraph = new FileInputStream(file);
            ObjectInputStream loader = new ObjectInputStream(savedGraph);
            weighted_graph wg = new WGraph_DS();
            wg = (weighted_graph)loader.readObject();
            if(!g.equals(wg))
                this.init(wg);
            loader.close();
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
    /**
     * Dikjstra algorithm:
     * all the nodes inserted to minimum priority queue, tag's field represents the
     * distance from src. At the beginning, the tag of every node is infinity, without
     * src, whose distance is 0 of course.
     * For each node u, the methode scans every neighbor v: if v isn't "visited",
     * the methode compares between tag of v, to: (tag of u)+length of (u,v) edge:
     * if tag of v shorter, we will update v's tag = (tag of u)+length of (u,v) edge,
     * v's parent = u.
     * After the methode scanned every neighbor, u is "visited".
     * If we have node u, which no has neighbors, his tag remain infinity and hasIsolated is true
     * (actually, the graph isn't connected).
     * @return
     */
    public boolean dikjstra(int src) {
        if (g != null) {
            boolean hasIsolated = false;
            PriorityQueue<node_info> Q = new PriorityQueue<node_info>();
            for (node_info n : g.getV()) {
                ((WGraph_DS.NodeInfo)n).setVisited(false);
                ((WGraph_DS.NodeInfo)n).setParent(null);
                if (n.getKey() == src)
                    n.setTag(0);
                else
                    n.setTag(Double.POSITIVE_INFINITY);
                Q.add(n);
            }
            while (!Q.isEmpty()) {
                node_info u = Q.poll();
                for (node_info v : g.getV(u.getKey())) {
                    if (!((WGraph_DS.NodeInfo) v).getVisited()) {
                        double t = u.getTag() + g.getEdge(u.getKey(), v.getKey());
                        if (v.getTag() > t) {
                            Q.remove(v);
                            v.setTag(t);
                            ((WGraph_DS.NodeInfo)v).setParent(u);
                            Q.add(v);
                        }
                    }
                }
                ((WGraph_DS.NodeInfo) u).setVisited(true);
                if (g.getV(u.getKey()).isEmpty())
                    hasIsolated = true;
            }
            return hasIsolated;
        }
        return false;
    }
}
