/**
 * 
 */
package org.zzz.jds.task.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zzz.jds.dag.Dag;
import org.zzz.jds.dag.Vertex;
import org.zzz.jds.dag.exception.DagCycleException;
import org.zzz.jds.dag.exception.DuplicatedEdgeException;
import org.zzz.jds.task.DagTaskConfig;
import org.zzz.jds.task.Task;
import org.zzz.jds.task.TaskFactory;
import org.zzz.jds.task.Util;
import org.zzz.jds.task.VertexTaskConfig;

/**
 * End to end test for the dag scheduler.
 * @author ming luo
 *
 */
public class EndToEndTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

    @Test
    public void test4VertexTask() throws DagCycleException, DuplicatedEdgeException, InterruptedException, ExecutionException {
        //1. create callable task
        Callable<Boolean> obj1 = () -> { System.out.println(1); Util.sleepSeconds(2); return true; };
        Callable<Boolean> obj2 = () -> { System.out.println(2); Util.sleepSeconds(3); return true; };
        Callable<Boolean> obj3 = () -> { System.out.println(3); Util.sleepSeconds(0); return true; };
        Callable<Boolean> obj4 = () -> { System.out.println(4); Util.sleepSeconds(0); return true; };

        //2. create task config
        VertexTaskConfig<Callable<Boolean>> v1 = new VertexTaskConfig<>(obj1);
        VertexTaskConfig<Callable<Boolean>> v2 = new VertexTaskConfig<>(obj2);
        VertexTaskConfig<Callable<Boolean>> v3 = new VertexTaskConfig<>(obj3);
        VertexTaskConfig<Callable<Boolean>> v4 = new VertexTaskConfig<>(obj4);

        DagTaskConfig dagTaskConfig = new DagTaskConfig();

        //3. create vertices
        Dag<DagTaskConfig> fourVerticeDag = new Dag<>(dagTaskConfig);
        Vertex<VertexTaskConfig<Callable<Boolean>>> N1 = new Vertex<>(v1);
        Vertex<VertexTaskConfig<Callable<Boolean>>> N2 = new Vertex<>(v2);
        Vertex<VertexTaskConfig<Callable<Boolean>>> N3 = new Vertex<>(v3);
        Vertex<VertexTaskConfig<Callable<Boolean>>> N4 = new Vertex<>(v4);

        //4. create edges between vertice
        fourVerticeDag.putEdge(N1, N2);
        fourVerticeDag.putEdge(N2, N3);
        fourVerticeDag.putEdge(N2, N4);

        assert(fourVerticeDag.getVertices().size() == 4);

        //5. build tasks
        Task tasks = TaskFactory.build(fourVerticeDag);

        ExecutorService service =  Executors.newSingleThreadExecutor();
        Future<Boolean> future = service.submit(tasks);
        Util.sleepSeconds(7);
        future.get();
    }

}
