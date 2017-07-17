import java.io.IOException;
import java.util.Vector;

import jeasy.analysis.MMAnalyzer;

/**
 * 中文分词器.
 */
public final class ChineseSpliter {
	private MMAnalyzer analyzer;
	
    public ChineseSpliter() {
    	//参数为分词粒度：当字数等于或超过该参数，且能成词，该词就被切分出来
    	analyzer = new MMAnalyzer(2);
    }

    /**
     * 对给定的文本进行中文分词.
     * 
     * @param text
     *            给定的文本
     * @param splitToken
     *            用于分割的标记,如"|"
     * @return 分词完毕的文本
     */
    public String split(final String text, final String splitToken) {
        String result = null;
        
        try {
            result = analyzer.segment(text, splitToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 去掉停用词.
     * 
     * @param oldText
     *            给定的文本
     * @return 去停用词后结果
     */
    public static String[] dropStopWords(final String[] oldText) {
        Vector<String> v1 = new Vector<String>();
        for (int i = 0; i < oldText.length; ++i) {
            if (!StopWordsHandler.isStopWord(oldText[i])) { // 不是停用词
                v1.add(oldText[i]);
            }
        }
        String[] newText = new String[v1.size()];
        v1.toArray(newText);
        return newText;
    }
}
