/**
 * 
 */
package org.zzz.jds.dag;

import java.util.Map;
import java.util.UUID;

/**
 * @author ming luo
 *
 */
public interface DagRelationship extends Relationship {
    public void setVertexInDag(Map<UUID, Vertex<?>> m);

}
