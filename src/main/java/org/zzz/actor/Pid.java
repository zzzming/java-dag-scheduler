/**
 * 
 */
package org.zzz.actor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ming luo
 *
 */
public final class Pid {

    private Map<String, UUID> pidMap = new ConcurrentHashMap<>();
    private Map<UUID, Actor>  actors = new ConcurrentHashMap<>();

    /**
     * Private constructor.
     */
    private Pid() {}

    /**
     * Thread safe singleton.
     * 
     * @return the ResultProcessPlugin
     */
    public static Pid getInstance() {
        return Loader.instance;
    }

    /**
     * Java memory model guarantees this class initialization.
     */
    private static class Loader {
        private static final Pid instance = new Pid();
    }

    public void register(String alias, Actor actor) {
        if (null == pidMap.putIfAbsent(alias, actor.getPid())) {
            register(actor);
        }
    }
    public void register(Actor actor) {
        actors.putIfAbsent(actor.getPid(), actor);
    }
    public UUID getPid(String alias) {
        return pidMap.get(alias);
    }
    public void send (UUID toId, Object message, UUID fromId) {
        new Thread(() -> {
           this.actors.get(toId).receive(fromId, message);
        }).start();
    }
    public void send (String alias, Object message, UUID fromId) {
       Optional<UUID> op = Optional.of(getPid(alias));
       if (op.isPresent()) {
           send(op.get(), message, fromId);
       }
    }
    public int getActorSize() {
       return this.actors.size();
    }
}
