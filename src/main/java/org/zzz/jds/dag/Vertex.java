/**
 * TODO: add license.
 */
package org.zzz.jds.dag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.zzz.jds.dag.exception.DuplicatedEdgeException;

/**
 * Vertex refers to a node in the graph theory.
 * A vertex can also contain another graph (namely DAG).
 * @author ming luo
 *
 */
public class Vertex <T> extends Element {

    private Map<UUID, Edge> inDegree  = new HashMap<>();
    private Map<UUID, Edge> outDegree = new HashMap<>();
    T task;

    public Vertex(T t) {
       super();
       task = t;
    }
    private Vertex(Map<UUID, Edge> inDegrees, Map<UUID, Edge> outDegrees, UUID uuid) {
       inDegree.putAll(inDegrees);
       outDegree.putAll(outDegrees);
       setUUID(uuid);
    }
    public void addTask(T t) {
       task = t;
    }
    public T getTask() {
       return task;
    }

    public void addInDegree(Edge e) throws DuplicatedEdgeException {
        checkDuplicatedEdge(e, inDegree);
        inDegree.put(e.getId(), e);
    }
    public void addOutDegree(Edge e) throws DuplicatedEdgeException {
        checkDuplicatedEdge(e, outDegree);
        outDegree.put(e.getId(), e);
    }
    public void removeInDegree(Edge e) {
        removeInDegree(e.getId());
    }
    public void removeOutDegree(Edge e) {
        removeOutDegree(e.getId());
    }
    public void removeInDegree(UUID id) {
        inDegree.remove(id);
    }
    public void removeOutDegree(UUID id) {
        outDegree.remove(id);
    }

    public void clearEdges() {
        inDegree.clear();
        outDegree.clear();
    }

    public Map<UUID, Edge> getInDegree() {
        return inDegree;
    }
    public Map<UUID, Edge> getOutDegree() {
        return outDegree;
    }
    /**
     * Soft clone only clones the edges and the vertex UUID.
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Vertex softClone() {
        return new Vertex(inDegree, outDegree, getId());
    }
    /**
     * If T is a relationship interface, vertex can apply its relationship to T.
     * Relationship refers to parent and child UUID.
     */
    public void applyRelationship() {
        if (task instanceof Relationship) {
            Relationship r = (Relationship) task;
            this.getOutDegree().values().parallelStream().forEach(e -> r.addChild(e.getToV()));
            this.getInDegree().values().parallelStream().forEach(e -> r.addParent(e.getFromV()));
            r.setId(getId());
        }
    }
    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            return this.getId().equals(((Vertex)o).getId());
        } else {
            return false;
        }
    }
    private void checkDuplicatedEdge(Edge e, Map<UUID, Edge> degrees) 
            throws DuplicatedEdgeException {
        if (edgeExists(e, degrees)) {
            throw new DuplicatedEdgeException("node " + getId());
        }
    }
    public boolean edgeExists(Edge e, Map<UUID, Edge> degrees) {
        return degrees.values().parallelStream().
           filter(edge -> e.equals(edge)).findAny().isPresent();
    }
}
