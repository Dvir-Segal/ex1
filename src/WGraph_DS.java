package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph, Serializable {
    public static class NodeInfo implements node_info, Comparable, Serializable {
        private int key;
        private HashMap<Integer, node_info> friends = new HashMap<>();
        private HashMap<Integer, Double> edges = new HashMap<>();
        private String data;
        private double tag;
        private node_info parent = null;
        private boolean visited;
        private static int counter = 0;
        /**
         * Constructor for NodeInfo. the key defined by general counter.
         * @return
         */
        public NodeInfo() {
            this.key = counter;
            counter++;
        }
        /**
         * Copy Constructor.
         * * @param n.
         * @return
         */
        public NodeInfo(NodeInfo n) {
            this.key = n.getKey();
            this.data = n.getInfo();
            this.tag = n.getTag();
        }
        /**
         * Constructor by only key.
         * * @param n.
         * @return
         */
        public NodeInfo(int key) {
            this.key = key;
        }
        /**
         * Return the key (id) associated with this node.
         * @return
         */
        @Override
        public int getKey() {
            return this.key;
        }
        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        @Override
        public String getInfo() {
            return this.data;
        }
        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.data = s;
        }
        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        @Override
        public double getTag() {
            return this.tag;
        }
        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }
        /**
         * The "parent" value of given node
         * @return
         */
        public node_info getParent(){ return this.parent;}
        /**
         * Allows changing the "parent" value of given node.
         */
        public void setParent (node_info p){ this.parent = p; }
        /**
         * getVisited is true if we scanned evert neighbor.
         */
        public boolean getVisited(){ return this.visited;}
        /**
         * setVisited is true if we scanned evert neighbor.
         * @return
         */
        public void setVisited (boolean visit){ this.visited = visit;}

        public String toString() {
            return "NodeInfo{key=" + key + '}';
        }
        /**
         * Comparing by tag, for priority queue at Dikjstra algorithm.
         * @return
         */
        @Override
        public int compareTo(Object o) {
            node_info n = (node_info)o;
            if (this.getTag() > n.getTag())
                return 1;
            if (this.getTag() < n.getTag())
                return -1;
            if (this.getKey() > n.getKey())
                return 1;
            return -1;
        }
    }

    private HashMap<Integer, node_info> group = new HashMap();
    private int counterChange =  0;
    private int counterEdge =0;

    /**
     * Constructor for graph.
     */
    public WGraph_DS(){}
    /**
     * Copy constructor for graph.
     * @param g
     * @return
     */
    public WGraph_DS(weighted_graph g) throws ArithmeticException{
        if (g!=null){
            for(node_info e: g.getV())
                this.addNode(e.getKey());
            for (node_info e: g.getV()){
                for(node_info j: ((NodeInfo)e).friends.values()){
                    this.connect(j.getKey(), e.getKey(), g.getEdge(e.getKey(), j.getKey()));
                }
            }
        }
        this.counterChange = 0;
        this.counterEdge = g.edgeSize();

    }
    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if (this.group.containsKey(key))
            return group.get(key);
        return null;
    }
    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (this.group.containsKey(node1) && this.group.containsKey(node2)){
            if (node1 == node2)
                return false;
            return ((NodeInfo)this.group.get(node1)).friends.containsKey(node2);
        }
        return false;
    }
    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (!hasEdge(node1, node2))
            return -1;
        return ((NodeInfo)this.group.get(node1)).edges.get(node2);
    }
    /**
     * add a new node to the graph with the given key.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!this.group.containsKey(key)){
            node_info n = new NodeInfo(key);
            this.group.put(key, n);
            counterChange++;

        }
    }
    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w)throws ArithmeticException{
        try{
            if (w<0)
                throw new ArithmeticException();
        }
        catch (ArithmeticException e){
            return;
        }
        if (!hasEdge(node1, node2)){
            if (this.group.containsKey(node1) && this.group.containsKey(node2) && node1!= node2){
                node_info n1 = this.group.get(node1);
                node_info n2 = this.group.get(node2);
                ((NodeInfo)n1).friends.put(node2, getNode(node2));
                ((NodeInfo)n1).edges.put(node2, w);
                ((NodeInfo)n2).friends.put(node1, getNode(node1));
                ((NodeInfo)n2).edges.put(node1, w);
                this.counterEdge++;
                this.counterChange++;
            }
        }
        else{
            node_info n1 = this.group.get(node1);
            node_info n2 = this.group.get(node2);
            if (((NodeInfo)n1).edges.get(node2)!=w){
                ((NodeInfo)n1).edges.put(node2, w);
                ((NodeInfo)n2).edges.put(node1, w);
                this.counterChange++;
            }
        }
    }
    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return this.group.values();
    }
    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (this.group.containsKey(node_id)){
            node_info n = this.group.get(node_id);
            return ((NodeInfo)n).friends.values();
        }
        return null;
    }
    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_info removeNode(int key) {
        if (!group.containsKey(key))
            return null;
        node_info x = this.group.get(key);
        for(node_info n: ((NodeInfo)x).friends.values()){
            ((NodeInfo)n).friends.remove(key);
            ((NodeInfo)n).edges.remove(key);
            counterEdge--;
            counterChange++;
        }
        counterChange++;
        this.group.remove(key);
        return x;
    }
    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)){
            node_info n1 =this.group.get(node1);
            node_info n2 = this.group.get(node2);
            ((NodeInfo)n1).friends.remove(node2);
            ((NodeInfo)n1).edges.remove(node2);
            ((NodeInfo)n2).friends.remove(node1);
            ((NodeInfo)n2).edges.remove(node1);
            counterChange++;
            counterEdge--;
        }
    }
    /** return the number of vertices (nodes) in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return this.group.size();
    }
    /**
     * return the number of edges (undirectional graph).
     * @return
     */
    @Override
    public int edgeSize() {
        return this.counterEdge;
    }
    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return
     */
    @Override
    public int getMC() {
        return this.counterChange;
    }
    /**
     * Comparing between 2 graphs
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        if(counterEdge!=wGraph_ds.counterEdge || nodeSize()!=wGraph_ds.nodeSize())
            return false;
        for(Integer key: this.group.keySet()){
            if(!wGraph_ds.group.containsKey(key))
                return false;
            for (node_info nei: this.getV(key)){
                if (wGraph_ds.getNode(nei.getKey()) == null)
                    return false;
                if (!wGraph_ds.hasEdge(key, nei.getKey()))
                    return false;
                if (wGraph_ds.getEdge(key, nei.getKey())!=this.getEdge(key, nei.getKey()))
                    return false;
            }
        }
        return true;
    }
}



