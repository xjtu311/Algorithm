import java.io.IOException;
import java.util.Vector;

import jeasy.analysis.MMAnalyzer;

/**
 * ���ķִ���.
 */
public final class ChineseSpliter {
	private MMAnalyzer analyzer;
	
    public ChineseSpliter() {
    	//����Ϊ�ִ����ȣ����������ڻ򳬹��ò��������ܳɴʣ��ôʾͱ��зֳ���
    	analyzer = new MMAnalyzer(2);
    }

    /**
     * �Ը������ı��������ķִ�.
     * 
     * @param text
     *            �������ı�
     * @param splitToken
     *            ���ڷָ�ı��,��"|"
     * @return �ִ���ϵ��ı�
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
     * ȥ��ͣ�ô�.
     * 
     * @param oldText
     *            �������ı�
     * @return ȥͣ�ôʺ���
     */
    public static String[] dropStopWords(final String[] oldText) {
        Vector<String> v1 = new Vector<String>();
        for (int i = 0; i < oldText.length; ++i) {
            if (!StopWordsHandler.isStopWord(oldText[i])) { // ����ͣ�ô�
                v1.add(oldText[i]);
            }
        }
        String[] newText = new String[v1.size()];
        v1.toArray(newText);
        return newText;
    }
}
