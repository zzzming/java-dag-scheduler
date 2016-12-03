/**
 * 
 */
package org.zzz.jds.task;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.zzz.jds.dag.DagRelationship;
import org.zzz.jds.dag.Vertex;

/**
 * @author ming luo
 *
 */
public class DagTaskConfig extends VertexTaskConfig implements DagRelationship {

    @SuppressWarnings("rawtypes")
    Set<Task> containedTasks = new HashSet<>();
    public DagTaskConfig () {}
    @Override
    public void setVertexInDag(Map<UUID, Vertex<?>> m) {
        m.values().forEach( v -> {
            Object o = v.getTask();
            if (o instanceof Task) {
                containedTasks.add((Task) o);
            }
        });
    }

    public Set<Task> getContainedTasks() {
        return containedTasks;
    }
}
