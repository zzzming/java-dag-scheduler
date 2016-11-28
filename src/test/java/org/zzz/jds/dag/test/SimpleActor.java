/** 
 * 
 */
package org.zzz.jds.dag.test;

import java.util.UUID;

import org.zzz.actor.Actor;

/**
 * @author ming luo
 *
 */
public class SimpleActor implements Actor {

    private String receivedMsg;
    private UUID senderUUID;
    public SimpleActor(String alias) {
        senderUUID = UUID.randomUUID();
        register(alias);
    }
    @Override
    public void receive(UUID fromId, String message) {
        receivedMsg = message;
        senderUUID = fromId;
    }
    public UUID getSender() {
        return senderUUID;
    }
    public String getReceivedMsg() {
        return receivedMsg;
    }
    @Override
    public UUID getPid() {
        return senderUUID;
    }
}
