/**
 * TODO: add License
 */
package org.zzz.jds.dag;

import java.util.UUID;

/**
 * This class describes the edge attributes in the graph.
 * An edge connects from one vertex to another vertex.
 * An edge is directional.
 * @author ming luo
 *
 */
public final class Edge extends Element{

    private UUID fromV;
    private UUID toV;
    public UUID getFromV() {
        return fromV;
    }

    public UUID getToV() {
        return toV;
    }

    public Edge (UUID fromV, UUID toV) {
        super();
        this.fromV = fromV;
        this.toV = toV;
    }
    /**
     * Test if e is the same edge.
     * hasHash equals is not recommended due to object creation is not managed.
     * @param e
     * @return
     */
    public boolean isSameEdge(Edge e) {
        return this.fromV == e.getFromV()
                && this.toV == e.getToV();
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Edge) {
            return isSameEdge((Edge)o);
        } else {
            return false;
        }
    }
}
