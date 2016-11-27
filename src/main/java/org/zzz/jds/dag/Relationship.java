/**
 * 
 */
package org.zzz.jds.dag;

import java.util.Map;
import java.util.UUID;

/**
 * Relationship is an interface that defines generic T in the vertex can be applied parent and child
 * UUID of the vertex.
 * It can be used to manage dependency just by the generic class itself.
 * @author ming luo
 *
 */
public interface Relationship {

	public void addParent(UUID id);
    public void addChild(UUID id);
    public void setId(UUID id);

    public void setVertexInDag(Map<UUID, Vertex<?>> m);
}
