/**
 * 
 */
package org.zzz.jds.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.zzz.actor.Actor;
import org.zzz.actor.Pid;
import org.zzz.jds.dag.Relationship;
import org.zzz.jds.dag.Vertex;

/**
 * @author ming luo
 * @param <T>
 *
 */
public class Task implements Actor, Callable<Boolean> {

    CountDownLatch parentLatch;

    @SuppressWarnings("rawtypes")
    Set<Task> containedTasks = new HashSet<>();

    Pid pid = Pid.getInstance();
    VertexTaskConfig config;
    boolean isDag = false;
    private long runtimeTO = -1;

    public Task (VertexTaskConfig config) {
       this.isDag = config instanceof DagTaskConfig;
       this.config = config;
       parentLatch = new CountDownLatch(config.parents.size());
       pid.register(this);
    }

    public void addTaskVertices(Set<Task> tasks) {
       containedTasks.addAll(tasks);
    }
    public void setTimeout(long timeout) {
       runtimeTO = timeout;
    }
    @Override
    public Boolean call() throws Exception {
       long allocTimeout = runtimeTO > 0 ? Math.min(this.config.getTimeout(), runtimeTO)
    		   : this.config.getTimeout();
       long start = Util.getNowInSeconds();
       waitForParents(allocTimeout);

       long newTimeout = Util.getRemainingTimeout(allocTimeout, start);
       if (newTimeout < 1) return new Boolean(false);

       if(isDag) dag(newTimeout); else runTask(newTimeout);
       this.clearDependencies();
       return true;
    }

    private void waitForParents(long timeout) {
       try {
           parentLatch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
    }

    private void runTask(long timeout) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future f = executor.submit((Callable) config.getTask());
        try {
            f.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {

            return;
        }
    }

    private void dag(long timeout) {
        long start = Util.getNowInSeconds();
        int threadPoolSize = containedTasks.size();
        if (threadPoolSize < 1) {
        	return;
        }
        Set<Future<Boolean>> futures = new HashSet<>();
        ExecutorCompletionService<Boolean> ecs = 
               new ExecutorCompletionService<>(Executors.newFixedThreadPool(threadPoolSize));
        containedTasks.forEach(t -> {
           futures.add(ecs.submit(t));
        });

        int completed = 0;
        while (completed < threadPoolSize) {
            if (Util.isTimedOut(start, timeout)) break;
            Optional<Future<Boolean>> o = Optional.of(ecs.poll());
            if (o.isPresent()) completed++;
            Util.sleepMilliseconds(10);
        }
    }

    private void clearDependencies() {
       this.config.children.parallelStream().forEach(e -> 
           this.send((UUID)e, new CompletionMessage(getPid()))
       );
    }
    @Override
    public UUID getPid() {
        return this.config.getPid();
    }

    @Override
    public void receive(UUID fromId, Object message) {
        if (message instanceof CompletionMessage) {
            CompletionMessage msg = (CompletionMessage)message;
            if (config.removeParent(msg.getFromUUID())) {
                parentLatch.countDown();
            }
        }
    }

    @Override
    public long getTimeoutMilli() {
       return this.config.getTimeout();
    }

}
