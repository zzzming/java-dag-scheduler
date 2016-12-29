/**
 * 
 */
package org.zzz.jds.dag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.zzz.jds.dag.exception.DagCycleException;
import org.zzz.jds.dag.exception.DuplicatedEdgeException;
import org.zzz.jds.dag.exception.NonexistentVertexException;

/**
 * Direct acyclic graph
 * In this design, a DAG can also be a vertex possibly in another DAG.
 * @author ming luo
 *
 */
public class Dag <T> extends Vertex {
    //keep track all contained vertices
    private Map<UUID, Vertex<?>> vertices = new HashMap<>();

    public Dag(T t) {
        super(t);
    }

    /**
     * Put an edge between two vertices.
     * @param fromV
     * @param toV
     * @throws DagCycleException
     * @throws DuplicatedEdgeException 
     */
    public void putEdge(Vertex<?> fromV, Vertex<?> toV)
            throws DagCycleException, DuplicatedEdgeException {
        testSelfLoop(fromV, toV);

        Edge e = new Edge(fromV.getId(), toV.getId());
        fromV.addOutDegree(e);
        toV.addInDegree(e);
        boolean fromVNew = addVertex(fromV);
        boolean toVNew = addVertex(toV);

        if (!isDag()) {
            //roll back changes if the new vertices and edge introduce a loop.
            fromV.removeOutDegree(e);
            toV.removeInDegree(e);
            if (fromVNew) vertices.remove(fromV.getId());
            if (toVNew)   vertices.remove(toV.getId());
            throw new DagCycleException("A cycle is detected.");
        }
    }

    public void removeEdge(Vertex<?> fromV, Vertex<?> toV) throws NonexistentVertexException {
        testVertexExist(fromV);
        testVertexExist(toV);

        Edge e = new Edge(fromV.getId(), toV.getId());
        fromV.removeOutDegree(e);
        toV.removeInDegree(e);

    }
    private void testVertexExist(Vertex v) throws NonexistentVertexException {
        if (!vertices.containsKey(v.getId()))
            throw new NonexistentVertexException("Nonexistent vertext " + v.getId());
    }

    private void testSelfLoop(Vertex<?> fromV, Vertex<?> toV)
            throws DagCycleException {
        if (fromV == toV)
            throw new DagCycleException("No self loop is permitted.");
    }

    /**
     * 
     * @param v
     * @return true if it's a new vertex.
     */
    public boolean addVertex(Vertex<?> v) {
        if (!vertices.containsKey(v.getId())) {
            vertices.put(v.getId(), v);
            return true;
        }
        return false;
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
        //soft clone all vertices in the dag just for topological sorting
        Map<UUID, Vertex<?>> map = this.vertices.entrySet().parallelStream().
                collect(Collectors.toMap( p -> p.getKey(), p -> p.getValue().softClone()));
        Queue<Vertex<?>> s = topologicalSorting(map,
                 new LinkedBlockingQueue<>());
        return !s.isEmpty();
    }
    /**
     * topological sort algorithm
     * @return
     */
    public Queue<Vertex<?>> topologicalSorting(Map<UUID, Vertex<?>> nodes, Queue<Vertex<?>> sorted) {
        //nodes -> all remaining vertices in the graph to be sorted
        Queue<Vertex<?>> s = new LinkedBlockingQueue<>(getRootVertices(nodes.values()));
        if (s.isEmpty()) {
            //cycle detected when nodes are not empty() so return empty sets since only to sort a dag
            //this is an error condition
            return nodes.isEmpty() ? sorted : new LinkedBlockingQueue<>();
        }

        s.parallelStream().forEach( n -> {
             n.getOutDegree().values().stream().forEach( e->
                  nodes.get(e.getToV()).removeInDegree(e.getId())
             );
             sorted.add(n);
             nodes.remove(n.getId());
        });
        return topologicalSorting(nodes, sorted);
    }
    /**
     * return a list of root vertices. Root vertices are vertex which has only out degree edges but no in degree edges.
     * @param vPool
     * @return
     */
    public static Set<Vertex<?>> getRootVertices(Collection<Vertex<?>> vPool) {
        return vPool.parallelStream().
            filter(v -> v.getInDegree().size() == 0).
            collect(Collectors.toSet());
    }
    public Set<Vertex<?>> getRootVertices() {
        return getRootVertices(this.vertices.values());
    }
    /**
     * Remove the root and its associated in-degree against the receiving vertices.
     * @param vPool
     */
    public void romoveRootVertices(Collection<Vertex<?>> vPool) {
        vPool.parallelStream().forEach( n -> {
            n.getOutDegree().values().stream().forEach( e->
                 this.vertices.get(e.getToV()).removeInDegree(e.getId())
            );
            this.vertices.remove(n.getId());
       });
    }
    /**
     * If T is a relationship interface, vertex can apply its relationship to T.
     * Recursively apply relationships to all the vertices.
     * Relationship refers to parent and child UUID.
     */
    public void applyRelationship() {
        this.vertices.values().forEach(v -> v.applyRelationship());
        if (task instanceof DagRelationship) {
            DagRelationship r = (DagRelationship) task;
            r.setVertexInDag(this.vertices);
        }
        super.applyRelationship();
    }
}
