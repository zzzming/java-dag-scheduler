/**
 * 
 */
package org.zzz.jds.dag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author ming luo
 *
 */
public class Dag extends Vertex {
    private Map<Integer, Vertex<?>> vertices = new HashMap<>();

    public Dag(Integer id) {
        super(id);
    }

    public void putEdge(Vertex<?> fromV, Vertex<?> toV)
            throws DagCycleException {
        if (fromV == toV)
            throw new DagCycleException("self loop is not allowed");
        Edge e = new Edge(fromV.getId(), toV.getId());
        if (!vertices.containsKey(fromV.getId())) {
            vertices.put(fromV.getId(), fromV);
        }
        if (!vertices.containsKey(toV.getId())) {
            vertices.put(toV.getId(), toV);
        }
        fromV.addOutDegree(e);
        toV.addInDegree(e);
        if (!isDag()) {
            throw new DagCycleException("self loop is not allowed");
        }
    }
    /**
     * @return the vertices
     */
    public Collection<Vertex<?>> getVertices() {
       return vertices.values();
    }
    /**
     * @param vertices the vertices to set
     */
    public void addVertices(Vertex<?> vertex) {
        this.vertices.put(vertex.getId(), vertex);
    }
    /**
     * @param vertices the vertices to set
     */
    public void removeVertices(Vertex<?> vertices) {
        this.vertices.remove(vertices);
    }
    /**
     * verify the di-graph is a dag
     * @return
     */
    public boolean isDag() {
         Queue<Vertex<?>> s = topologicalSorting(new HashMap<>(this.vertices),
                 new LinkedBlockingQueue<>());//.isEmpty();
         System.out.println(s.toString());
         return !s.isEmpty();
    }
    /**
     * topological sort algorithm
     * @return
     */
    public Queue<Vertex<?>> topologicalSorting(Map<Integer, Vertex<?>> nodes, Queue<Vertex<?>> sorted) {
        Queue<Vertex<?>> s = new LinkedBlockingQueue<>(buildRootVertices(nodes.values()));
        if (s.isEmpty()) {
            //cycle detected when nodes are not empty() so return empty sets since only to sort a dag
            //this is an error condition
            return nodes.isEmpty() ? sorted : new LinkedBlockingQueue<>();
        }
//        s.stream().forEach(v -> 
//                                (Map<Integer, Edge> e = v.getOutDegree();
//                                nodes.remove(v.getId());
        for (Vertex<?> v : s) {
             for (Edge e : v.getOutDegree().values()) {
                  nodes.get(e.getToV()).removeInDegree(e.getId());
             }
             sorted.add(v);
             nodes.remove(v.getId());
        }
        return topologicalSorting(nodes, sorted);
    }
    public Set<Vertex<?>> buildRootVertices(Collection<Vertex<?>> vPool) {
        return vPool.parallelStream().
            filter(v -> v.getInDegree().size() == 0).
            collect(Collectors.toSet());
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        Vertex<Integer> N1 = new Vertex<>(1);
        Vertex<Integer> N2 = new Vertex<>(2);
        Vertex<Integer> N3 = new Vertex<>(3);

        Dag dag = new Dag(90);
        try {
			dag.putEdge(N1, N2);
			dag.putEdge(N2, N3);
		} catch (DagCycleException e) {
			e.printStackTrace();
		}

    }

}
