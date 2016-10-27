/**
 * 
 */
package org.zzz.jds.dag;

/**
 * @author ming luo
 *
 */
public final class Edge {

    private Integer fromV;
    private Integer toV;
    private Integer id;
    public Integer getFromV() {
        return fromV;
    }

    public Integer getToV() {
        return toV;
    }

    public Edge (Integer fromV, Integer toV) {
        this.fromV = fromV;
        this.toV = toV;
        this.id = fromV + toV;
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
    public boolean equals(Object o) {
        if (o instanceof Edge) {
            return isSameEdge((Edge)o);
        } else {
            return false;
        }
    }
    public Integer getId() {
        return id;
    }
}
