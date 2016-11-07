/**
 * TODO: add license.
 */
package org.zzz.jds.dag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Vertex refers to a node in the graph theory.
 * A vertex can also contain another graph (namely DAG).
 * @author ming luo
 *
 */
public class Vertex <T> extends Element {

    private Map<UUID, Edge> inDegree  = new HashMap<>();
    private Map<UUID, Edge> outDegree = new HashMap<>();

    public Vertex() {
    	super();
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
    public boolean equals(Object o) {
        if (o instanceof Vertex) {
        	Vertex v = (Vertex)o;
            return this.getId().equals(v.getId());
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
