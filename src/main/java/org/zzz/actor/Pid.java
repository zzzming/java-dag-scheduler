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

    public UUID register(String alias, Actor actor) {
        UUID id = UUID.randomUUID();
        return register(alias, id, actor);
    }
    public UUID register(String alias, UUID id, Actor actor) {
        this.pidMap.put(alias, id);
        return register(id, actor);
    }
    public UUID register(UUID id, Actor actor) {
        this.actors.put(id, actor);
        return id;
    }
    public UUID register(Actor actor) {
        UUID id = UUID.randomUUID();
        return register(id, actor);
    }
    public UUID getPid(String alias) {
        return pidMap.get(alias);
    }
    public void send (UUID fromId, String message, UUID toId) {
        new Thread(() -> {
           this.actors.get(fromId).receive(fromId, message);
        }).start();
    }
    public void send (String alias, String message, UUID toId) {
        new Thread(() -> {
           Optional<UUID> op = Optional.of(getPid(alias));
           if (op.isPresent()) {
               UUID fromId = op.get();
               this.actors.get(fromId).receive(fromId, message);
           }
        }).start();
    }
}
