/**
 * 
 */
package org.zzz.jds.dag.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.zzz.jds.dag.Dag;
import org.zzz.jds.dag.Vertex;
import org.zzz.jds.dag.exception.DagCycleException;
import org.zzz.jds.dag.exception.DuplicatedEdgeException;

/**
 * @author ming luo
 *
 */
public class DagUnitTest {

    private Dag<Integer> fiveVerticeDag = new Dag<>(new Integer(0));
    Vertex<Integer> N1 = new Vertex<>(new Integer(0));
    Vertex<Integer> N2 = new Vertex<>(new Integer(0));
    Vertex<Integer> N3 = new Vertex<>(new Integer(0));
    Vertex<Integer> N4 = new Vertex<>(new Integer(0));
    Vertex<Integer> N5 = new Vertex<>(new Integer(0));
    /**
     * @throws java.lang.Exception
     * 
     */
    @Before
    public void setUp() throws Exception {
        fiveVerticeDag.putEdge(N1, N2);
        fiveVerticeDag.putEdge(N2, N3);
        fiveVerticeDag.putEdge(N1, N4);
        fiveVerticeDag.putEdge(N4, N5);
        fiveVerticeDag.putEdge(N5, N3);
    }

    @Test (expected=DagCycleException.class)
    public void testFailSelfLoopVertex() throws DagCycleException, DuplicatedEdgeException{
        Vertex<Integer> n1 = new Vertex<>(new Integer(0));
        Vertex<Integer> n2 = new Vertex<>(new Integer(0));
        Dag<Integer> dag = new Dag<>(new Integer(0));
        dag.putEdge(n1, n2);
        dag.putEdge(n2, n1);

        assertFalse(dag.isDag());
    }

    @Test (expected=DagCycleException.class)
    public void testOneSelfLoopVertex() throws DagCycleException, DuplicatedEdgeException{
    	Vertex<Integer> n1 = new Vertex<>(new Integer(0));
        Dag<Integer> dag = new Dag<>(new Integer(0));
        dag.putEdge(n1, n1);

        assertFalse(dag.isDag());
    }

    @Test
    public void test5VertexDag() {
        assertTrue(fiveVerticeDag.isDag());
    }

    @Test
    public void testAddVertex5VertexDag() throws DagCycleException, DuplicatedEdgeException {
        Vertex<Integer> n6 = new Vertex<>(new Integer(0));
        fiveVerticeDag.putEdge(N5, n6);
        fiveVerticeDag.putEdge(N3, n6);
        assertTrue(fiveVerticeDag.isDag());
    }

    @Test (expected=DagCycleException.class)
    public void testAddLoopin5VertexDag() throws DagCycleException, DuplicatedEdgeException {
        fiveVerticeDag.putEdge(N5, N1);
        assertFalse(fiveVerticeDag.isDag());
    }

    @Test (expected=DuplicatedEdgeException.class)
    public void testAddDuplicateEdgein5VertexDag() throws DagCycleException, DuplicatedEdgeException {
        fiveVerticeDag.putEdge(N2, N3);
        assertTrue(fiveVerticeDag.isDag());
    }
}
