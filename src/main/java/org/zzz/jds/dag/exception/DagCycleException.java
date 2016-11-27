/**
 * 
 */
package org.zzz.jds.dag.exception;

/**
 * @author ming luo
 *
 */
public final class DagCycleException extends Exception{

    public DagCycleException(String string) {
        super(string);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 2114201844906144728L;

}
