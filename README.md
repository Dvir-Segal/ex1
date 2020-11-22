In our assignment, we suppose to build and define data structure for weighted graph, and commit operations on weighted graph.
We heve 3 interfaces: node_info,  weighted_graph and weighted_graph_algorithms, which are implemented by 3 classes: NodeInfo (inner class in Wgraph_DS), Wgraph_DS and  WGraph_Algo.
Our central data structure is HashMap, whose complexity of searching, insertion and retrieval in O(1).

For each node, we build 2 HashMaps: one for the neighbors (called "freinds"), and the other one for the edges (called "edges").
For example: node 0 connected nodes 1, 2, 4. The weight of (0,1) is 3.5, the weight of (0,2) is 11, the weight of (0,4) is 16:
"friends" with the keys 1, 2, 4 handle the values: node 1, node 2, node 4. "edges" with the keys 1, 2, 4 handle the values: 3.5, 11, 16.
Every weighted graph has HashMap of "group": all the nodes that included in the weighted graph.

File with the graph for "Wgraph_AlgoTest" is added here.


