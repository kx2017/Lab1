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
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**.
 *
 *
 * @author Gorge Bush
 */
public class Graph {
  /**.
   *
   *
   * @author Gorge Bush
   */
  public static final String FORMAT = "[^ a-zA-Z,.?!:;\"]+";
  /**.
   *
   *
   * @author Gorge Bush
   */
  public static final String TYPE = "jpg";
  /**.
   *
   *
   * @author Gorge Bush
   */
  public static final int MAX = 99999;
  /**.
   *
   *
   * @author Gorge Bush
   */
  private transient List<String> words; // �����б�
  /**.
   *
   *
   * @author Gorge Bush
   */
  private transient Map<String, Integer> wordMap; // ������ӳ��Ϊ���
  /**.
   *
   *
   * @author Gorge Bush
   */
  private transient int[][] value; // ��ʾͼ�Ķ�ά����
  /**.
   *
   *
   * @author Gorge Bush
   */
  private transient boolean[][] pathFlag; // ��ʾ���·���ľ��󣬱��ڽ�·��������ʾ
  /**.
   *
   *
   * @author Gorge Bush
   */
  private transient ShowImage myImage; // ��ʾ���·���ľ��󣬱��ڽ�·��������ʾ

  /**
   * ��ȡ�ļ��������ļ�����.
   *
   * @param filePath Ҫ��ȡ���ļ�·��
   * @return ��ȡ�ɹ�����true
   */
  private boolean createDirectedGraph(final String filename) {
    final File inputFile = new File(filename);
    try {
      final BufferedReader buffReader =
          new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "utf8"));
      words = new ArrayList<>();
      wordMap = new HashMap<>();
      final List<String> tempWordList = new ArrayList<>(); // ��ʱ�������е��ʣ��Ӷ������ߺ�Ȩ��
      String line;
      while (buffReader.ready()) {
        // ���ж���
        final String inLine = buffReader.readLine();
        if (inLine == null) {
          continue;
        } else {
          line = inLine.replaceAll(FORMAT, "");
        }
        line = line.replaceAll("\\W+", " ");
        final String[] lineArray = line.split(" ");
        for (String word : lineArray) {
          if (!word.equals("")) { // �ո���
            word = word.toLowerCase(Locale.US); // ����ΪСд
            tempWordList.add(word);
            if (!wordMap.containsKey(word)) {
              // ��δ����words�������µ�ӳ��
              words.add(word);
              wordMap.put(word, words.size() - 1);
            }
          }
        }
      }
      // �������󲢼���Ȩ��
      value = new int[words.size()][words.size()];
      pathFlag = new boolean[words.size()][words.size()];
      for (int i = 0; i < words.size(); i++) {
        for (int j = 0; j < words.size(); j++) {
          pathFlag[i][j] = false;
        }
      }
      String startWord = words.get(0); // �ߵ����
      for (int i = 1; i < tempWordList.size(); i++) {
        final String endWord = tempWordList.get(i);
        value[wordMap.get(startWord)][wordMap.get(endWord)]++; // Ȩ��+1
        startWord = endWord;
      }
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
   * ��ѯ�������ʼ���ŽӴ�.
   *
   * @param tmpStartWord ����1
   * @param tmpEndWord ����2
   * @return ����1��2�粻�����򷵻�Null�����򷵻��ŽӴ�List
   */
  public List<String> queryBridgeWords(final String tmpStartWord, final String tmpEndWord) {
    final String startWord = tmpStartWord.replaceAll(FORMAT, "");
    final String endWord = tmpEndWord.replaceAll(FORMAT, "");
    if (wordMap.containsKey(startWord) && wordMap.containsKey(endWord)) {
      final List<String> result = new ArrayList<>();
      final int startIndex = wordMap.get(startWord); // ���
      final int endIndex = wordMap.get(endWord); // �յ�
      for (int i = 0; i < value[startIndex].length; i++) {
        // �������������ڽӵ�
        if (value[startIndex][i] > 0 && value[i][endIndex] > 0) {
          // iΪ�Žӵ�
          result.add(words.get(i));
        }
      }
      return result;
    } else {
      return null;
    }
  }

  /**
   * �����ı������ŽӴʻ�����ı�.
   *
   * @param tmpOldText ���ı�
   * @return ���ı�
   */
  public String generateNewText(final String tmpOldText) {
    final StringBuilder builder = new StringBuilder("");
    final String oldText = tmpOldText.replaceAll(FORMAT, "");
    final String[] wordArray = oldText.replaceAll("\\W+", " ").replaceAll("^ ", "")
        .replaceAll("$ ", "").toLowerCase(Locale.US).split(" "); // �ݴ���
    String start = wordArray[0];
    final Random random = new Random();
    for (int i = 1; i < wordArray.length; i++) {
      final String end = wordArray[i];
      final List<String> bridgeWords = queryBridgeWords(start, end); // �ŽӴ��б�
      builder.append(start);
      builder.append(' ');
      if (bridgeWords != null && !bridgeWords.isEmpty()) { // �����ŽӴ�
        final int rand =
            (random.nextInt() % bridgeWords.size() + bridgeWords.size()) % bridgeWords.size();//���
        builder.append(bridgeWords.get(rand));
        builder.append(' ');
      }
      start = end;
    }
    builder.append(wordArray[wordArray.length - 1]); // �����һ�����ʼ���
    return builder.toString();
  }

  /**
   * Dijkstra����������������·��.
   *
   * @param startV ���
   * @param endV �յ�
   */
  public void dijkstra(final int startV, final int endV, final String label) {
    final int wordSize = words.size();
    int[] path = new int[wordSize];
    int[] dist = new int[wordSize];
    boolean[] visited = new boolean[wordSize];
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
      // ѭ�������v0��������Ľڵ�prev����̾���min
      int min = wordSize;
      for (int j = 0; j < wordSize; j++) {
        if (!visited[j] && dist[j] < min) {
          min = dist[j];
          prev = j;
        }
      }
      visited[prev] = true;
      // ����prev�����������нڵ㵽v0��ǰ���ڵ㼰����
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
        System.out.println(words.get(startV) + " -> " + words.get(pend) + " ���ɴ�\n");
      } else {
        System.out.println(words.get(startV) + " -> " + words.get(pend)
            + " ���·���ĳ�Ϊ" + dist[pend]);
        stack.clear();
        int u = pend;
        int v;
        while (u != startV) { // ��·��ѹջ
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

        final String dotFormat = getAllPath();
        createDotGraph(dotFormat, "DotGraph" + label);
      }
    }
    for (int j = 0; j < words.size(); j++) {
      for (int k = 0; k < words.size(); k++) {
        if (value[j][k] == MAX) {
          value[j][k] = 0;
        }
      }
    }
  }

  /**
   * ������ߣ��������ʼ�������ɵ��ı������ո�ֹͣ����.
   *
   *
   * <p>startWord ��ʼ����
   */
  public void randomWalk(Scanner cin) {
    final List<String> tempWords = new ArrayList<>(); // ��ʱ����startWord����ָ������е���
    int[][] flag = new int[words.size()][words.size()]; // ��־���Ƿ��Ѿ������ʹ�
    String tempWord = null;
    final Random random = new Random();
    final int startNode = (random.nextInt() % words.size() + words.size()) % words.size(); // ���
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
        // ���ڳ���
        final int rand =
            (random.nextInt() % tempWords.size() + tempWords.size()) % tempWords.size(); // ���
        tempWord = tempWords.get(rand);
        if (flag[start][rand] == 0) {
          flag[start][rand]++;
        } else {
          break;
        }
      }
      startWord = tempWord;
      final String input = cin.nextLine();
      if (input.equals("")) { // ��ENTER����������
        System.out.print(tempWord + " ");
      } else { // �������+ENTER��ֹͣ����
        break;
      }
    } while (tempWords != null && !tempWords.isEmpty()); // �����ڳ���ʱ����ѭ��
    System.out.println("\n�������");
  }

  /**
   * ��ͼ����Ϊ.jpg�ļ�����.
   *
   * @param dotFormat ͼ������·�����ַ���
   * @param fileName ���ɵ�ͼ�ļ�
   */
  public static void createDotGraph(String dotFormat, String fileName) {
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
   * �õ�ͼ�����бߵ��ַ���.
   *
   * @return ���ַ���
   */
  public String getAllPath() { // �õ�ͼ�����бߣ���д��dot�﷨��ʽ���ַ���
    String paths;
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
   * ����ͼ�ļ�.
   */
  public void showDirectedGraph() {
    final String dotFormat = getAllPath();
    createDotGraph(dotFormat, "DotGraph");
    myImage = new ShowImage("DotGraph.jpg");
  }

  /**
   * �õ������ʼ�����·��.
   *
   * @param tmpWord1 ��ʼ����
   * @param tmpWord2 ��ֹ����
   */
  public void calcShortestPath(String tmpWord1, String tmpWord2) {
    final String word1 = tmpWord1.replaceAll(FORMAT, "");
    final String word2 = tmpWord2.replaceAll(FORMAT, "");
    if (words.contains(word1) && words.contains(word2)) {
      dijkstra(wordMap.get(word1), wordMap.get(word2), "Calc");
      myImage.setImage("DotGraphCalc.jpg");
    } else {
      System.out.println("No " + word1 + " or " + word2 + " in the graph!");
    }
  }

  /**
   * �õ�ĳ���ʵ����ⵥ�ʼ�����·��.
   *
   * @param tmpWord1 ��ʼ����
   */
  public void calcShortestPath(String tmpWord1) {
    final String word1 = tmpWord1.replaceAll(FORMAT, "");
    if (words.contains(word1)) {
      dijkstra(wordMap.get(word1), -1, word1 + "ToAll");
      myImage.setImage("DotGraph" + word1 + "To" + "All" + ".jpg");
    } else {
      System.out.println("No " + word1 + " in the graph!");
    }
  }

  /**
   * ���Ժ���.
   */
  public void test() {
    return;
  }

  /**
   * ���������.
   *
   * @param args ϵͳ����
   */
  public static void main(String[] args) {
    final Graph graph = new Graph();
    System.out.print("�������ļ�������.txt��β����");
    final Scanner in = new Scanner(System.in);
    String filename = in.next();
    File file = new File(filename);
    while (!file.exists()) {
      System.out.print("�ļ������ڣ����������룺");
      filename = in.next();
      file = new File(filename);
    }
    graph.createDirectedGraph(filename);
    graph.showDirectedGraph();
    boolean flag = false;
    while (!flag) {
      System.out.println("1.��ѯ�ŽӴ�");
      System.out.println("2.�����ŽӴ��������ı�");
      System.out.println("3.������������֮������·��");
      System.out.println("4.����ĳ�����ʵ���һ���ʵ����·��");
      System.out.println("5.������ߣ���ENTER��������������������ߣ�");
      System.out.print("��ѡ��Ҫִ�еĲ���: ");
      final String input = in.next();
      String startWord;
      String endWord;
      switch (input) {
        case "1":
          System.out.print("�������������ʣ��Կո��������");
          startWord = in.next();
          endWord = in.next();
          final List<String> bridgeWords = graph.queryBridgeWords(startWord, endWord);
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
          System.out.print("������Ҫת�����ı���");
          String inText = new String();
          inText = in.nextLine();
          System.out.println(graph.generateNewText(inText));
          break;
        case "3":
          System.out.print("�������������ʣ��Կո��������");
          startWord = in.next();
          endWord = in.next();
          graph.calcShortestPath(startWord, endWord);
          break;
        case "4":
          System.out.print("��������ʼ���ʣ�");
          startWord = in.next();
          graph.calcShortestPath(startWord);
          break;
        case "5":
          graph.randomWalk(in);
          break;
        default:
          flag = true;
          break;
      }
    }
    in.close();
  }
}
