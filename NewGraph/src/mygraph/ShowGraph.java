package mygraph;

import java.io.File;
import java.util.List;

/**
 * 将有向图生成为.jpg文件保存并展示
 * @author ZhaoYang
 *
 */
public class ShowGraph {
	public static final String FORMAT = "[^ a-zA-Z,.?!:;\"]+";
	
	public static final String TYPE = "jpg";
	
	public static final int MAX = 99999;
	
	private transient ShowImage myImage;
	
	/**
	 * 将图生成为.jpg文件保存.
	 *
	 * @param dotFormat 图中所有路径的字符串
	 * @param fileName 生成的图文件
	 */
	public void createDotGraph(String dotFormat, String fileName) {
	    GraphViz gv = new GraphViz();
	    gv.addln(gv.start_graph());
	    gv.add(dotFormat);
	    gv.addln(gv.end_graph());
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    final File out = new File(fileName + "." + TYPE);
	    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), TYPE), out);
	}

	/**
	 * 得到图中所有边的字符串.
	 * 
	 * @param mygraph 存储图信息的有向图
	 * @return 边字符串
	 */
	public String getAllPath(DirectedGraph mygraph) { // 得到图的所有边，并写成dot语法形式的字符串
	    String paths;
	    List<String> words = mygraph.getWords();
	    int[][] value = mygraph.getValue();
	    boolean[][] pathFlag = mygraph.getPathFlag();
	    final StringBuilder tempPath = new StringBuilder();
	    for (int i = 0; i < words.size(); i++) {
	        for (int j = 0; j < words.size(); j++) {
		        if (value[i][j] != 0 && value[i][j] != MAX) {
		            if (pathFlag[i][j]) {
		        	  tempPath.append(words.get(i) + "->" + words.get(j) + "[label=\"" + value[i][j]
		                  + "\", color=\"red\"];");
		            } else {
		              tempPath
		                  .append(words.get(i) + "->" + words.get(j) + "[label=\"" + value[i][j] + "\"];");
		            }
	            }
	        }
	    }
	    paths = tempPath.toString();
	    return paths;
	}

	/**
	 * 创建.dot文件.
	 * 
	 * @param mygraph 存储图信息的有向图
	 */
	public void showDirectedGraph(DirectedGraph mygraph) {
	    final String dotFormat = getAllPath(mygraph);
	    createDotGraph(dotFormat, "DotGraph");
	    myImage = new ShowImage("DotGraph.jpg");
	}
}
