/**
 * 
 */
package org.zzz.actor;

import java.util.UUID;

/**
 * @author ming luo
 *
 */
public interface Actor {

    public UUID getPid();

    public void receive(UUID fromId, Object message);

    default public void register(String alias) {
        Pid.getInstance().register(alias, this);
    }
    default public long getTimeoutMilli() {
        return 0;
    }

    default public void send(UUID toId, Object message) {
       Pid.getInstance().send(toId, message, getPid());
    }
    default public void send(String toAlias, Object message) {
        Pid.getInstance().send(toAlias, message, getPid());
    }

}
