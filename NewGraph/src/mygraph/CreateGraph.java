package mygraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

/**
 * 根据读入的文本文件生成有向图的控制类
 * @author ZhaoYang
 *
 */
public class CreateGraph {
	private static final String FORMAT = "[^ a-zA-Z,.?!:;\"]+";
	
	private List<String> tempWordList;
	
	/**
	 * 读取文件，处理文件内容
	 * 
	 * @param mygraph 要存储信息的有向图
	 * @param filePath 要读取的文件路径
	 * @return 读取成功返回true
	 */
	public boolean readFile(DirectedGraph mygraph, final String filename) {
	    final File inputFile = new File(filename);
	    try {
	        final BufferedReader buffReader =
	            new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "utf8"));
	        List<String> words = new ArrayList<>();
	        Map<String, Integer> wordMap = new HashMap<>();
	        tempWordList = new ArrayList<>();  // 临时保存所有单词，从而分析边和权重
	        String line;
	        while (buffReader.ready()) {  // 按行读入
		        final String inLine = buffReader.readLine();
		        if (inLine == null) {
		            continue;
		        } else {
		            line = inLine.replaceAll(FORMAT, "");
		        }
		        line = line.replaceAll("\\W+", " ");
		        final String[] lineArray = line.split(" ");
		        for (String word : lineArray) {
		            if (!word.equals("")) { // 空格处理
			            word = word.toLowerCase(Locale.US); // 均视为小写
			            tempWordList.add(word);
			            if (!wordMap.containsKey(word)) {
			              // 尚未加入words，建立新的映射
			              words.add(word);
			              wordMap.put(word, words.size() - 1);
			            }
		            }
		        }
	        }
	        mygraph.setWords(words);
	        mygraph.setWordMap(wordMap);
	        buffReader.close();
	    } catch (FileNotFoundException e) {
	        return false;
	    } catch (UnsupportedEncodingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return false;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	/**
	 * 根据读入的单词生成有向图
	 * 
	 * @param mygraph 要存储信息的有向图
	 */
	public void createDirectedGraph(DirectedGraph mygraph) {
		List<String> words = mygraph.getWords();
		Map<String, Integer> wordMap = mygraph.getWordMap();
		// 建立矩阵并计算权重
        int[][] value = new int[words.size()][words.size()];
        boolean[][] pathFlag = new boolean[words.size()][words.size()];
        for (int i = 0; i < words.size(); i++) {
	        for (int j = 0; j < words.size(); j++) {
	          pathFlag[i][j] = false;
	        }
        }
        String startWord = words.get(0); // 边的起点
        for (int i = 1; i < tempWordList.size(); i++) {
	        final String endWord = tempWordList.get(i);
	        value[wordMap.get(startWord)][wordMap.get(endWord)]++; // 权重+1
	        startWord = endWord;
        }
        mygraph.setValue(value);
        mygraph.setPathFlag(pathFlag);
	}
}
