package mygraph;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述桥接词列表的实体类
 * @author ZhaoYang
 *
 */
public class BridgeWords {
	/**
	 * 起始单词
	 */
	private String startWord;
	
	/**
	 * 终止单词
	 */
	private String endWord;
	
	/**
	 * 桥接词列表
	 */
	private List<String> wordlist;
	
	/**
	 * 构造函数 
	 */
	BridgeWords(){
		wordlist = new ArrayList<>();
	}
	
	/**
	 * 带有参数列表的构造函数
	 * @param startWord
	 * @param endWord
	 */
	BridgeWords(String startWord, String endWord){
		this.startWord = startWord;
		this.endWord = endWord;
		wordlist = new ArrayList<>();
	}
	
	/**
	 * 设置startWord的属性值
	 * @param startWord
	 */
	public void setStartWord(String startWord) {
		this.startWord = startWord;
	}
	
	/**
	 * 获取startWord的属性值
	 * @return
	 */
	public String getStartWord() {
		return this.startWord;
	}
	
	/**
	 * 设置endWord的属性值
	 * @param endWord
	 */
	public void setEndWord(String endWord) {
		this.endWord =  endWord;
	}
	
	/**
	 * 获取endWord的属性值
	 * @return
	 */
	public String getEndWord() {
		return this.endWord;
	}
	
	/**
	 * 设置wordlist的属性值
	 * @param wordlist
	 */
	public void setWordlist(List<String> wordlist) {
		this.wordlist = wordlist;
	}
	
	/**
	 * 获取wordlist的属性值
	 * @return
	 */
	public List<String> getWordlist() {
		return this.wordlist;
	}
}
