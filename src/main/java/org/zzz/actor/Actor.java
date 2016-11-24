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
    public void receive(UUID fromId, String message);
    public long getTimeoutMilli();
}
