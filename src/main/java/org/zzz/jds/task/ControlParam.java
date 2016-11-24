/**
 * 
 */
package org.zzz.jds.task;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ming luo
 *
 */
public class ControlParam {

    private long timeout;
    private long loops = 1;
    private boolean loopsParallable = false;
    public ControlParam(long to, long numberOfLoops) {
        timeout = to;
        loops = numberOfLoops;
    }
    public long getTimeout() {
        return timeout;
    }
    public long getNumberOfLoops() {
        return loops;
    }
    /**
     * @return the loopsParallable
     */
    public boolean isLoopsParallable() {
        return loopsParallable;
    }
    /**
     * @param loopsParallable the loopsParallable to set
     */
    public void setLoopsParallable(boolean loopsParallable) {
        this.loopsParallable = loopsParallable;
    }
}
