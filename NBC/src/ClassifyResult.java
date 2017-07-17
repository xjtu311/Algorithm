/**
 * 分类结果.
 */
public class ClassifyResult {
    /** 分类的概率. */
    public double probility;
    /** 类别名. */
    public String classification;

    /** 构造函数. */
    public ClassifyResult() {
        this.probility = 0;
        this.classification = null;
    }
}
