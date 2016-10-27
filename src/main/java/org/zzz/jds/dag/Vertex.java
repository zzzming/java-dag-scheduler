/**
 * 
 */
package org.zzz.jds.dag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.graph.EndpointPair;

/**
 * Vertex is a concept in the graph theory.
 * A vertex can also contain another graph (namely DAG).
 * @author ming luo
 *
 */
public class Vertex <T> {

    private Map<Integer, Edge> inDegree  = new HashMap<>();
    private Map<Integer, Edge> outDegree = new HashMap<>();

    private Integer id;

    public Vertex(Integer id) {
        this.id = id;
    }

    public void addInDegree(Edge e) {
        inDegree.put(e.getId(), e);
    }
    public void addOutDegree(Edge e) {
       outDegree.put(e.getId(), e);
    }
    public void removeInDegree(Edge e) {
        removeInDegree(e.getId());
    }
    public void removeOutDegree(Edge e) {
        removeOutDegree(e.getId());
    }
    public void removeInDegree(Integer id) {
        inDegree.remove(id);
    }
    public void removeOutDegree(Integer id) {
        outDegree.remove(id);
    }

    public void clearEdges() {
        inDegree.clear();
        outDegree.clear();
    }
    public Integer getId() {
        return id;
    }

    public Map<Integer, Edge> getInDegree() {
        return inDegree;
    }
    public Map<Integer, Edge> getOutDegree() {
        return outDegree;
    }
}
