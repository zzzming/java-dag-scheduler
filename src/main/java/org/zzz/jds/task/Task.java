/**
 * 
 */
package org.zzz.jds.task;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.zzz.actor.Actor;
import org.zzz.actor.Pid;
import org.zzz.jds.dag.Relationship;
import org.zzz.jds.dag.Vertex;

/**
 * @author ming luo
 * @param <T>
 *
 */
public class Task implements Actor, Relationship {

	final static int INFINITE = -1;
    Set<UUID> parents = new HashSet<>();
    Set<UUID> children = new HashSet<>();
    @SuppressWarnings("rawtypes")
    Set<Task> containedTasks = new HashSet<>();

    UUID id;
    Pid pid = Pid.getInstance();
    Callable<Integer> call;
    long timeout = INFINITE;

    public Task (Callable<Integer> thread, long timeout) {
       this.call = thread;
       this.timeout = timeout;
       pid.register(this);
    }
    public Task (Callable<Integer> thread) {
       this(thread, INFINITE);
    }

    public void execute(long timeout) {
       long allocTimeout = this.timeout == INFINITE ? timeout : Math.min(this.timeout, timeout);
       //1. launch dag which contains a set of Vertex and their edge relations
       dag(allocTimeout);
    }

    public Callable<Integer> getCallable() {
       return this.call;
    }

    private void dag(long timeout) {
       int threadPoolSize = containedTasks.size();
       if (threadPoolSize > 0) {
           Set<Future<Integer>> futures = new HashSet<>();
           ExecutorCompletionService<Integer> ecs = 
                   new ExecutorCompletionService<>(Executors.newFixedThreadPool(threadPoolSize));
           containedTasks.forEach(t -> {
               futures.add(ecs.submit(t.getCallable()));
           });

           for (int i = 0; i < futures.size(); ++i) {
               Future<Integer> r = ecs.poll();
           }
       }
    }

	@Override
	public UUID getPid() {
		return this.id;
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

    @Override
    public void addParent(UUID id) {
        parents.add(id);
    }

    @Override
    public void addChild(UUID id) {
        children.add(id);
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
    @Override
    public void setVertexInDag(Map<UUID, Vertex<?>> m) {
        m.values().forEach( v -> {
            Object o = v.getTask();
            if (o instanceof Task) {
                containedTasks.add((Task) o);
            }
        });
    }

}
