/**
 * 
 */
package org.zzz.jds.task;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.zzz.jds.dag.Dag;
import org.zzz.jds.dag.Vertex;

/**
 * @author ming luo
 *
 */
public class TaskFactory {
    static Task build(Dag<DagTaskConfig> dag) {
        Set<Task> tasks = new HashSet<>();
        dag.applyRelationship();
        dag.getVertices().parallelStream().forEach( v -> {
            tasks.add(new Task((VertexTaskConfig)v.getTask()));
        });
        Task dagTask = new Task((DagTaskConfig)dag.getTask());
        dagTask.addTaskVertices(tasks);
        return dagTask;
    }
}
