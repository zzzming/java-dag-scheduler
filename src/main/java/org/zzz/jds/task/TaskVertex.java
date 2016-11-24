/**
 * 
 */
package org.zzz.jds.task;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.zzz.actor.Actor;
import org.zzz.actor.Pid;
import org.zzz.jds.dag.Dag;

/**
 * @author ming luo
 *
 */
public class TaskVertex<Task> extends Dag<Task> implements Actor {

    Set<UUID> inDegree = new HashSet<>();
    Set<UUID> outDegree = new HashSet<>();
    Pid pid = Pid.getInstance();

    public TaskVertex (Task task) {
       super(task);
       pid.register(this.getId(), this);
       inDegree.addAll(this.getInDegree().values());
    }

    public void run(long timeout) {
       Task task = (Task) this.getTask();
       if (task instanceof EmptyTask) {
          //TODO launch all tasks
       }
    }

	@Override
	public UUID getPid() {
		return this.getId();
	}

	@Override
	public void receive(UUID fromId, String message) {
        switch (message) {
        case "clear":
        }
	}

	@Override
	public long getTimeoutMilli() {
		// TODO Auto-generated method stub
		return 0;
	}

}
