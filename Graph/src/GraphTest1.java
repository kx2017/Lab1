import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GraphTest1 {

	@Test
	public void testGenerateNewText() {
		Graph graph = new Graph();
		graph.createDirectedGraph("test.txt");
		String result = graph.generateNewText("out new and new civilizations hdfsjak");
		System.out.println(result);
		assertEquals("out new to and new and civilizations hdfsjak", result);
	}
}
