/**
 * 
 */
package org.zzz.jds.task;

import java.util.HashSet;
import java.util.Set;

import org.zzz.jds.dag.Dag;

/**
 * Task factory builds a task that can run independently.
 * @author ming luo
 *
 */
public class TaskFactory {
    static public Task build(Dag<DagTaskConfig> dag) {
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
