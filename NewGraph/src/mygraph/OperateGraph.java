package mygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 * 对生成的有向图的各种操作，包括查询桥接词，生成新文本，计算最短路径，随机游走
 * @author ZhaoYang
 *
 */
public class OperateGraph {
	public static final String FORMAT = "[^ a-zA-Z,.?!:;\"]+";
	
	public static final int MAX = 99999;
	
	private transient ShowImage myImage;
	
	/**
	 * 查询两个单词间的桥接词.
	 *
	 * @param mygraph 存储图信息的有向图
	 * @param bridgeWord 存储桥接词的桥接词列表
	 */
	public void queryBridgeWords(DirectedGraph mygraph, BridgeWords bridgeWord) {
	    List<String> words = mygraph.getWords();
	    Map<String, Integer> wordMap = mygraph.getWordMap();
	    int[][] value = mygraph.getValue();
	    String tmpStartWord = bridgeWord.getStartWord();
	    String tmpEndWord = bridgeWord.getEndWord();
		final String startWord = tmpStartWord.replaceAll(FORMAT, "");
	    final String endWord = tmpEndWord.replaceAll(FORMAT, "");
	    if (wordMap.containsKey(startWord) && wordMap.containsKey(endWord)) {
	        final List<String> result = new ArrayList<>();
	        final int startIndex = wordMap.get(startWord); // 起点
	        final int endIndex = wordMap.get(endWord); // 终点
	        for (int i = 0; i < value[startIndex].length; i++) {
		        // 遍历起点的所有邻接点
		        if (value[startIndex][i] > 0 && value[i][endIndex] > 0) {
		          // i为桥接点
		          result.add(words.get(i));
		        }
	        }
	        bridgeWord.setWordlist(result);
	    } else {
	    	bridgeWord.setWordlist(null);
	    }
	}

	/**
	 * 根据文本及其桥接词获得新文本.
	 *
	 * @param mygraph 存储图信息的有向图
	 * @param tmpOldText 旧文本
	 * @return 新文本
	 */
	public String generateNewText(DirectedGraph mygraph, final String tmpOldText) {
	    final StringBuilder builder = new StringBuilder("");
	    final String oldText = tmpOldText.replaceAll(FORMAT, "");
	    final String[] wordArray = oldText.replaceAll("\\W+", " ").replaceAll("^ ", "")
	        .replaceAll("$ ", "").toLowerCase(Locale.US).split(" "); // 容错处理
	    String start = wordArray[0];
	    final Random random = new Random();
	    for (int i = 1; i < wordArray.length; i++) {
		    BridgeWords bridgeWord = new BridgeWords(start, wordArray[i]);
		    queryBridgeWords(mygraph, bridgeWord); // 桥接词列表
		    List<String> bridgeWordlist = bridgeWord.getWordlist();
		    builder.append(start);
		    builder.append(' ');
		    if (bridgeWordlist != null && !bridgeWordlist.isEmpty()) { // 存在桥接词
		        final int rand =
		            (random.nextInt() % bridgeWordlist.size() + bridgeWordlist.size()) % bridgeWordlist.size();//随机
		        builder.append(bridgeWordlist.get(rand));
		        builder.append(' ');
	        }
	        start = bridgeWord.getEndWord();
	    }
	    builder.append(wordArray[wordArray.length - 1]); // 把最后一个单词加上
	    return builder.toString();
	}

	/**
	 * Dijkstra方法查找两点间最短路径
	 * 
	 * @param mygraph 存储图信息的有向图
	 * @param startV 起点
	 * @param endV 终点
	 * @param label 标志是两点间最短路径还是单源最短路径
	 */
	public void dijkstra(DirectedGraph mygraph, final int startV, final int endV, final String label) {
	    List<String> words = mygraph.getWords();
	    int[][] value = mygraph.getValue();
	    boolean[][] pathFlag = mygraph.getPathFlag();
		final int wordSize = words.size();
	    int[] path = new int[wordSize];
	    int[] dist = new int[wordSize];
	    boolean[] visited = new boolean[wordSize];
	    //int[][] value = new int[wordSize][wordSize];
	    for (int j = 0; j < words.size(); j++) {
	        for (int k = 0; k < words.size(); k++) {
		        if (value[j][k] == 0) {
		          value[j][k] = MAX;
		        }
	        }
	    }
	    int prev = 0;
	    for (int i = 0; i < dist.length; i++) {
	        path[i] = startV;
	        dist[i] = value[startV][i];
	        visited[i] = false;
	    }
	    visited[startV] = true;
	    for (int v = 1; v < wordSize; v++) {
	        // 循环求得与v0距离最近的节点prev和最短距离min
	        int min = wordSize;
	        for (int j = 0; j < wordSize; j++) {
	            if (!visited[j] && dist[j] < min) {
		            min = dist[j];
		            prev = j;
	            }
	        }
	        visited[prev] = true;
	        // 根据prev修正其他所有节点到v0的前驱节点及距离
	        for (int k = 0; k < wordSize; k++) {
		        if (!visited[k] && (min + value[prev][k]) < dist[k]) {
		            path[k] = prev;
		            dist[k] = min + value[prev][k];
		        }
	        }
	    }
	    for (int j = 0; j < wordSize; j++) {
	        for (int k = 0; k < wordSize; k++) {
	            pathFlag[j][k] = false;
	        }
	    }
	    int st = endV;
	    int ed = endV + 1;
	    if (endV == -1) {
	        st = 0;
	        ed = words.size();
	    }
	    final Stack<Integer> stack = new Stack<Integer>();
	    for (int pend = st; pend < ed; pend++) {
	        if (dist[pend] == MAX) {
	            System.out.println(words.get(startV) + " -> " + words.get(pend) + " 不可达\n");
	        } else {
	            System.out.println(words.get(startV) + " -> " + words.get(pend)
	                + " 最短路径的长为" + dist[pend]);
		        stack.clear();
		        int u = pend;
		        int v;
		        while (u != startV) { // 将路径压栈
		            stack.push(u);
		            v = u;
		            u = path[u];
		            pathFlag[u][v] = true;
		        }
		        stack.push(startV);
		        while (!stack.empty()) {
		            System.out.print(words.get(stack.pop()));
		            if (!stack.empty()) {
		                System.out.print(" -> ");
		            }
		        }
		        System.out.println("\n");
		        
		        ShowGraph show = new ShowGraph();
		        final String dotFormat = show.getAllPath(mygraph);
		        show.createDotGraph(dotFormat, "DotGraph" + label);
	        }
	    }
	}
	
	/**
	 * 得到两单词间的最短路径.
	 * 
	 * @param mygraph 存储图信息的有向图
	 * @param tmpWord1 起始单词
	 * @param tmpWord2 终止单词
	 * @return 计算结果提示信息
	 */
	public String calcShortestPath(DirectedGraph mygraph, String tmpWord1, String tmpWord2) {
	    final String word1 = tmpWord1.replaceAll(FORMAT, "");
	    final String word2 = tmpWord2.replaceAll(FORMAT, "");
	    List<String> words = mygraph.getWords();
	    Map<String, Integer> wordMap = mygraph.getWordMap();
	    String result = new String();
	    if (words.contains(word1) && words.contains(word2)) {
	        dijkstra(mygraph, wordMap.get(word1), wordMap.get(word2), "Calc");
	        myImage = new ShowImage("DotGraphCalc.jpg");
	        result = "Succeed to find shortest path between " + word1 + " and " + word2;
	    } else {
	        result = "No " + word1 + " or " + word2 + " in the graph!";
	    }
	    return result;
	}

	/**
	 * 得到某单词到任意单词间的最短路径.
	 * 
	 * @param mygraph 存储图信息的有向图
	 * @param tmpWord1 起始单词
	 * @return 计算结果提示信息
	 */
	public String calcShortestPath(DirectedGraph mygraph, String tmpWord1) {
	    final String word1 = tmpWord1.replaceAll(FORMAT, "");
	    List<String> words = mygraph.getWords();
	    Map<String, Integer> wordMap = mygraph.getWordMap();
	    String result = new String();
	    if (words.contains(word1)) {
	        dijkstra(mygraph, wordMap.get(word1), -1, word1 + "ToAll");
	        myImage = new ShowImage("DotGraph" + word1 + "To" + "All" + ".jpg");
	        result = "Succeed to find shortest path between " + word1 + " and all";
	    } else {
	        result = "No " + word1 + " in the graph!";
	    }
	    return result;
	}

	/**
	 * 随机游走，输出由起始单词生成的文本，按空格停止遍历.
	 * 
	 * @param mygraph 存储图信息的有向图
	 * @param cin 输入的字符
	 */
	public void randomWalk(DirectedGraph mygraph, Scanner cin) {
		List<String> words = mygraph.getWords();
		Map<String, Integer> wordMap = mygraph.getWordMap();
		int[][] value = mygraph.getValue();
	    final List<String> tempWords = new ArrayList<>(); // 临时保存startWord出边指向的所有单词
	    int[][] flag = new int[words.size()][words.size()]; // 标志边是否已经被访问过
	    String tempWord = null;
	    final Random random = new Random();
	    final int startNode = (random.nextInt() % words.size() + words.size()) % words.size(); // 随机
	    String startWord = words.get(startNode);
	    System.out.println(startWord + " ");
	    do {
	        final int start = wordMap.get(startWord);
	        tempWords.clear();
	        for (int i = 0; i < value[start].length; i++) {
	            if (value[start][i] != 0 && value[start][i] != MAX) {
	                tempWords.add(words.get(i));
	            }
	        }
	        if (tempWords == null || tempWords.isEmpty()) {
	            break;
	        } else {
	            // 存在出边
	            final int rand =
	                (random.nextInt() % tempWords.size() + tempWords.size()) % tempWords.size(); // 随机
	            tempWord = tempWords.get(rand);
	            if (flag[start][rand] == 0) {
	              flag[start][rand]++;
	            } else {
	              break;
	            }
	        }
	        startWord = tempWord;
	        final String input = cin.nextLine();
	        if (input.equals("")) { // 按ENTER键继续游走
	            System.out.print(tempWord + " ");
	        } else { // 按任意键+ENTER键停止游走
	            break;
	        }
	    } while (tempWords != null && !tempWords.isEmpty()); // 不存在出边时跳出循环
	    System.out.println("\n游走完毕");
	}
}
