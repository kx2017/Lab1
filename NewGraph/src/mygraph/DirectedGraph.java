package mygraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述有向图的实体类
 * @author ZhaoYang
 *
 */
public class DirectedGraph {
	/**
	 * 单词列表
	 */
	private List<String> words;
	
	/**
	 * 单词到索引的映射
	 */
	private Map<String, Integer> wordMap;
	
	/**
	 * 有向图的邻接矩阵
	 */
	private int[][] value;
	
	/**
	 * 表示最短路径的矩阵，便于将路径高亮显示
	 */
	private boolean[][] pathFlag;
	
	/**
	 * 构造函数
	 */
	DirectedGraph(){
		words = new ArrayList<>();
		wordMap = new HashMap<>();
	}
	
	/**
	 * 设置words的属性值
	 * @param words
	 */
	public void setWords(List<String> words) {
		this.words = words;
	}
	
	/**
	 * 获取words的属性值
	 * @return words
	 */
	public List<String> getWords(){
		return this.words;
	}
	
	/**
	 * 设置wordMap的属性值
	 * @param wordMap
	 */
	public void setWordMap(Map<String, Integer> wordMap) {
		this.wordMap = wordMap;
	}
	
	/**
	 * 获取wordMap的属性值
	 * @return wordMap
	 */
	public Map<String, Integer> getWordMap(){
		return this.wordMap;
	}
	
	/**
	 * 设置value的属性值
	 * @param value
	 */
	public void setValue(int[][] value) {
		this.value = value;
	}
	
	/**
	 * 获取value的属性值
	 * @return value
	 */
	public int[][] getValue(){
		return this.value;
	}
	
	/**
	 * 设置pathFlag的属性值
	 * @param pathFlag
	 */
	public void setPathFlag(boolean[][] pathFlag) {
		this.pathFlag = pathFlag;
	}
	
	/**
	 * 获取pathFlag的属性值
	 * @return
	 */
	public boolean[][] getPathFlag(){
		return this.pathFlag;
	}
}
