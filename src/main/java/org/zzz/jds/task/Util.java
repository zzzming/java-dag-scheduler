/**
 * 
 */
package org.zzz.jds.task;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author ming luo
 *
 */
public final class Util {
    /**
     * Check if the operation is timed out.
     * 
     * @param start in system seconds.
     * @param timeout
     * @return
     */
    public static boolean isTimedOut(long start, long timeout) {
        return (getNowInSeconds() - start) > timeout;
    }

    /**
     * Return the current time in seconds.
     * 
     * @return
     */
    public static long getNowInSeconds() {
        return getNowTimeUnit(TimeUnit.SECONDS);
    }
    /**
     * Get the current time in the specified time unit.
     * @param timeUnit
     * @return
     */
    public static long getNowTimeUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
        case NANOSECONDS : return TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
        case MICROSECONDS: return TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
        case MILLISECONDS: return TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis());
        //default is seconds
        case SECONDS : default: return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        case MINUTES : return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        case HOURS: return TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        case DAYS: return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
        }
    }
    /**
     * Sleep in the unit of Seconds.
     * @param seconds
     */
    public static void sleepSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }
    /**
     * Sleep in the unit of milliseconds.
     * @param milliseconds
     */
    public static void sleepMilliseconds(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Get the current running Java running Pid. WARNING: Linux only not
     * platform agnostic.
     * 
     * @return the pid in String.
     */
    public static String getPid() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
    /**
     * Get a remaining timeout given the original start time and time out seconds.
     * @param allocTimeout
     * @param start
     * @return
     */
    public static long getRemainingTimeout(long allocTimeout, long start) {
        return start + allocTimeout - getNowInSeconds();
    }
}
