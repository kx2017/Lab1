package mygraph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OperateGraphTest {
	private DirectedGraph mygraph;
	private OperateGraph operate;

	@Before
	public void setUp() throws Exception {
		mygraph = new DirectedGraph();
		CreateGraph create = new CreateGraph();  //生成有向图
	    create.readFile(mygraph, "test.txt");
	    create.createDirectedGraph(mygraph);
	    operate = new OperateGraph();
	}

	@Test
	public void testQueryBridgeWords() {
		BridgeWords bridge = new BridgeWords("new", "and");
		operate.queryBridgeWords(mygraph, bridge);
		String result = bridge.getWordlist().toString();
		System.out.println(result);
		assertEquals("[to, life]", result);
//		assertEquals(null, graph.queryBridgeWords("my", "you"));
//		assertEquals(null, graph.queryBridgeWords("new", "you"));
//		assertEquals(null, graph.queryBridgeWords("my", "and"));
	}

	@Test
	public void testGenerateNewText() {
		String oldText = "out new civilizations hdfsjak";
		String result = operate.generateNewText(mygraph, oldText);
		assertEquals("out new and civilizations hdfsjak", result);
	}

	@Test
	public void testCalcShortestPathDirectedGraphStringString() {
		String result = operate.calcShortestPath(mygraph, "to", "my");
		assertEquals("No to or my in the graph!", result);
		String result2 = operate.calcShortestPath(mygraph, "to", "new");
		assertEquals("Succeed to find shortest path between to and new", result2);
	}

	@Test
	public void testCalcShortestPathDirectedGraphString() {
		String result = operate.calcShortestPath(mygraph, "my");
		assertEquals("No my in the graph!", result);
		String result2 = operate.calcShortestPath(mygraph, "new");
		assertEquals("Succeed to find shortest path between new and all", result2);
	}

}
