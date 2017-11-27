package mygraph;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class ChooseOperation {
	/**
	 * 主程序入口.
	 *
	 * @param args 系统参数
	 */
	public static void main(String[] args) {
	    final DirectedGraph graph = new DirectedGraph();
	    System.out.print("请输入文件名（以.txt结尾）：");
	    final Scanner in = new Scanner(System.in);
	    String filename = in.next();
	    File file = new File(filename);
	    while (!file.exists()) {
	        System.out.print("文件不存在，请重新输入：");
	        filename = in.next();
	        file = new File(filename);
	    }
	    
	    CreateGraph create = new CreateGraph();  //生成有向图
	    create.readFile(graph, filename);
	    create.createDirectedGraph(graph);
	    ShowGraph show = new ShowGraph();  //展示有向图
	    show.showDirectedGraph(graph);
	    OperateGraph operate = new OperateGraph();
	    boolean flag = false;
	    while (!flag) {
	        System.out.println("1.查询桥接词");
	        System.out.println("2.根据桥接词生成新文本");
	        System.out.println("3.查找两个单词之间的最短路径");
	        System.out.println("4.查找某个单词到任一单词的最短路径");
	        System.out.println("5.随机游走（按ENTER键继续，任意键结束游走）");
	        System.out.print("请选择要执行的操作: ");
	        String startWord;
	        String endWord;
	        final String input = in.next();
	        switch (input) {
		        case "1":
		            System.out.print("请输入两个单词（以空格隔开）：");
		            BridgeWords bridge = new BridgeWords(in.next(), in.next());
		            //startWord = in.next();
		            //endWord = in.next();
		            operate.queryBridgeWords(graph, bridge);
		            final List<String> bridgeWords = bridge.getWordlist();
		            startWord = bridge.getStartWord();
		            endWord = bridge.getEndWord();
		            if (bridgeWords == null) {
		                System.out.println("No " + startWord + " or " + endWord + " in the graph!");
		            } else {
		                if (bridgeWords.size() == 0) {
		                    System.out.println("No bridge words from " + startWord + " to " + endWord);
			            } else {
			                System.out
			                    .println("The bridge words from " + startWord + " to " + endWord + " are: ");
			                for (final String i : bridgeWords) {
			                    System.out.print(i + " ");
			                }
			            }
			            System.out.print("\n");
			        }
		            System.out.print("\n");
		            break;
		        case "2":
		            in.nextLine();
		            System.out.print("请输入要转换的文本：");
		            String inText = new String();
		            inText = in.nextLine();
		            System.out.println(operate.generateNewText(graph, inText));
		            break;
		        case "3":
		            System.out.print("请输入两个单词（以空格隔开）：");
		            startWord = in.next();
		            endWord = in.next();
		            System.out.println(operate.calcShortestPath(graph, startWord, endWord));
		            break;
		        case "4":
		            System.out.print("请输入起始单词：");
		            startWord = in.next();
		            System.out.println(operate.calcShortestPath(graph, startWord));
		            break;
		        case "5":
		            operate.randomWalk(graph,in);
		            break;
		        default:
		            flag = true;
		            break;
		    }
	    }
	    in.close();
	}
}
