/**
 * 停用词处理器.
 * 
 * @author sunliyu
 */
public final class StopWordsHandler {
    /** 禁止实例化. */
    private StopWordsHandler() {
    }

    /** 常用停用词. */
    private static String[] stopWordsList = {
            // 来自 c:\Windows\System32\NOISE.CHS
            "的", "一", "不", "在", "人", "有", "是", "为", "以", "于", "上", "他", "而",
            "后", "之", "来", "及", "了", "因", "下", "可", "到", "由", "这", "与", "也",
            "此", "但", "并", "个", "其", "已", "无", "小", "我", "们", "起", "最", "再",
            "今", "去", "好", "只", "又", "或", "很", "亦", "某", "把", "那", "你", "乃",
            "它",
            // 来自网络
            "要", "将", "应", "位", "新", "两", "中", "更", "我们", "自己", "没有", "“", "”",
            "，", "（", "）", "" };

    /**
     * 判断一个词是否是停止词.
     * 
     * @param word
     *            要判断的词
     * @return 是停止词，返回true，否则返回false
     */
    public static boolean isStopWord(final String word) {
        for (int i = 0; i < stopWordsList.length; ++i) {
            if (word.equalsIgnoreCase(stopWordsList[i])) {
                return true;
            }
        }
        return false;
    }
}
