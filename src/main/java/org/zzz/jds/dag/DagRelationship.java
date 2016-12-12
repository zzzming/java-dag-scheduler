/**
 * 
 */
package org.zzz.jds.dag;

import java.util.Map;
import java.util.UUID;

/**
 * DAG relationship extends from vertex relationship to provide addtional
 * all vertices with in the DAG.
 * @author ming luo
 *
 */
public interface DagRelationship extends Relationship {
    public void setVertexInDag(Map<UUID, Vertex<?>> m);

}
