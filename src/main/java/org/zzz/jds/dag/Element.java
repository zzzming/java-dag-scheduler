/**
 * 
 */
package org.zzz.jds.dag;

import java.util.UUID;

/**
 * Describe the common attributes of Vertex and Edge.
 * @author ming luo
 *
 */
abstract public class Element {

    private UUID id = UUID.randomUUID();
    public UUID getId() {
        return this.id;
    }
}
