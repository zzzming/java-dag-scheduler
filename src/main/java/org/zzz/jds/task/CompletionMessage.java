/**
 * 
 */
package org.zzz.jds.task;

import java.util.UUID;

/**
 * @author ming luo
 *
 */
public class CompletionMessage {

    UUID id;
    public CompletionMessage(UUID uuid) {
        id = uuid;
    }
    public UUID getFromUUID() {
        return id;
    }
}
