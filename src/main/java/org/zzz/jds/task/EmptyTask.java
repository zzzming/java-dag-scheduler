/**
 * 
 */
package org.zzz.jds.task;

/**
 * @author ming luo
 *
 */
public class EmptyTask implements Task<Integer> {

	@Override
	public Integer call() throws Exception {
		// return immediately
		return 0;
	}

}
