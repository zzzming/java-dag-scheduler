/**
 * 
 */
package org.zzz.jds.task;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.zzz.actor.Actor;
import org.zzz.actor.Pid;
import org.zzz.jds.dag.Relationship;
import org.zzz.jds.dag.Vertex;

/**
 * @author ming luo
 *
 */
public class VertexTaskConfig<Callable> implements Relationship {

    final static int DEFAULT_ONE_MINUTE = 60;
    Set<UUID> parents = new HashSet<UUID>();
    Set<UUID> children = new HashSet<UUID>();
    boolean isDag = false;

    Callable callable;

    UUID id;
    //timeout must be enforced
    private long timeout = DEFAULT_ONE_MINUTE;

    public VertexTaskConfig (Callable t){
         callable = t;
    }
    public VertexTaskConfig (){
    }
    public Callable getTask() {
        return this.callable;
    }
    public void setTimeout(long to) {
        this.timeout = to;
    }
    public long getTimeout() {
        return this.timeout;
    }
    public UUID getPid() {
       return this.id;
    }
    public boolean removeParent(UUID id) {
       return parents.remove(id);
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
}
