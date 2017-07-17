import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * 朴素贝叶斯分类器.
 */
public class NaiveBayesClassifier {
	protected TrainnedModel model;
    
	protected transient IntermediateData db;
	
	/** 中文分词. */
    private transient ChineseSpliter textSpliter;

    public NaiveBayesClassifier() {
    	textSpliter = new ChineseSpliter();
    }
    
    /**
     * 加载数据模型文件.
     * 
     * @param modelFile
     *            模型文件路径
     */
	public final void loadModel(final String modelFile) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                    modelFile));
            model = (TrainnedModel) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	
    /**
     * 对给定的文本进行分类.
     * 
     * @param text
     *            给定的文本
     * @return 分类结果
     */
    public final String classify(final String text) {
        String[] terms = null;
        // 中文分词处理(分词后结果可能还包含有停用词）
        terms = textSpliter.split(text, " ").split(" ");
        terms = ChineseSpliter.dropStopWords(terms); // 去掉停用词，以免影响分类
        
        double probility = 0.0;
        List<ClassifyResult> crs = new ArrayList<ClassifyResult>(); // 分类结果
        for (int i = 0; i < model.classifications.length; i++) {
            // 计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
            probility = calcProd(terms, i);
            // 保存分类结果
            ClassifyResult cr = new ClassifyResult();
            cr.classification = model.classifications[i]; // 分类
            cr.probility = probility; // 关键字在分类的条件概率
            System.out.println(model.classifications[i] + "：" + probility);
            crs.add(cr);
        }
        
        // 找出最大的元素
        ClassifyResult maxElem = (ClassifyResult) java.util.Collections.max(
                crs, new Comparator() {
                    public int compare(final Object o1, final Object o2) {
                        final ClassifyResult m1 = (ClassifyResult) o1;
                        final ClassifyResult m2 = (ClassifyResult) o2;
                        final double ret = m1.probility - m2.probility;
                        if (ret < 0) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

        return maxElem.classification;
    }
    
    
    public final void train(String intermediateData, String modelFile) {
    	// 加载中间数据文件
    	loadData(intermediateData);
    	
    	model = new TrainnedModel(db.classifications.length);
    	
    	model.classifications = db.classifications;
    	model.vocabulary = db.vocabulary;
    	// 开始训练
    	calculatePc();
    	calculatePxc();
    	db = null;
    	
    	try {
    		// 用序列化，将训练得到的结果存放到模型文件中
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(modelFile));
            out.writeObject(model);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private final void loadData(String intermediateData) {
		try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
            		intermediateData));
            db = (IntermediateData) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    /** 计算先验概率P(c). */
    protected void calculatePc() {
    }
    
    /** 计算类条件概率P(x|c). */
    protected void calculatePxc() {
    }
    
    
    /**
     * 计算文本属性向量X在类Cj下的后验概率P(Cj|X).
     * 
     * @param x
     *            文本属性向量
     * @param cj
     *            给定的类别
     * @return 后验概率
     */
    protected double calcProd(final String[] x, final int cj) {
        return 0;
    }
    
    
    public final double getCorrectRate(String classifiedDir, String encoding, String model) {
        int total = 0;
        int correct = 0;
        
        loadModel(model);
        
        File dir = new File(classifiedDir);
        
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("训练语料库搜索失败！ [" + classifiedDir
                    + "]");
        }

        String[] classifications = dir.list();
        for (String c : classifications) {
            String[] filesPath = IntermediateData.getFilesPath(classifiedDir, c);
            for (String file : filesPath) {
                total++;
                String text = null;
                try {
                    text = IntermediateData.getText(file, encoding);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String classification = classify(text);
                if(classification.equals(c)) { // 计算出的类别，和原始的类别是否相同
                    correct++;
                }
            }
        }
        
        return ((double)correct/(double)total) * 100;
    }
    
 
    
    
    public static void test(NaiveBayesClassifier classifier, String[] args) {
    	long startTime = System.currentTimeMillis(); // 获取开始时间
        if (args[0].equals("-t")) { // 训练
            classifier.train(args[1], args[2]);
            System.out.println("训练完毕");
        } else if(args[0].equals("-r")) { // 获取正确率
            double ret = classifier.getCorrectRate(args[1], args[2], args[3]);
            System.out.println("正确率为：" + ret);
        } else { // 分类
            classifier.loadModel(args[0]);

            String text = null;
            try {
                text = IntermediateData.getText(args[1], args[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            String result = classifier.classify(text); // 进行分类

            System.out.println("此属于[" + result + "]");
        }

        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }
}
