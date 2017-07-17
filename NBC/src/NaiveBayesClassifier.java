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
 * ���ر�Ҷ˹������.
 */
public class NaiveBayesClassifier {
	protected TrainnedModel model;
    
	protected transient IntermediateData db;
	
	/** ���ķִ�. */
    private transient ChineseSpliter textSpliter;

    public NaiveBayesClassifier() {
    	textSpliter = new ChineseSpliter();
    }
    
    /**
     * ��������ģ���ļ�.
     * 
     * @param modelFile
     *            ģ���ļ�·��
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
     * �Ը������ı����з���.
     * 
     * @param text
     *            �������ı�
     * @return ������
     */
    public final String classify(final String text) {
        String[] terms = null;
        // ���ķִʴ���(�ִʺ������ܻ�������ͣ�ôʣ�
        terms = textSpliter.split(text, " ").split(" ");
        terms = ChineseSpliter.dropStopWords(terms); // ȥ��ͣ�ôʣ�����Ӱ�����
        
        double probility = 0.0;
        List<ClassifyResult> crs = new ArrayList<ClassifyResult>(); // ������
        for (int i = 0; i < model.classifications.length; i++) {
            // ����������ı���������terms�ڸ����ķ���Ci�еķ�����������
            probility = calcProd(terms, i);
            // ���������
            ClassifyResult cr = new ClassifyResult();
            cr.classification = model.classifications[i]; // ����
            cr.probility = probility; // �ؼ����ڷ������������
            System.out.println(model.classifications[i] + "��" + probility);
            crs.add(cr);
        }
        
        // �ҳ�����Ԫ��
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
    	// �����м������ļ�
    	loadData(intermediateData);
    	
    	model = new TrainnedModel(db.classifications.length);
    	
    	model.classifications = db.classifications;
    	model.vocabulary = db.vocabulary;
    	// ��ʼѵ��
    	calculatePc();
    	calculatePxc();
    	db = null;
    	
    	try {
    		// �����л�����ѵ���õ��Ľ����ŵ�ģ���ļ���
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
	
    /** �����������P(c). */
    protected void calculatePc() {
    }
    
    /** ��������������P(x|c). */
    protected void calculatePxc() {
    }
    
    
    /**
     * �����ı���������X����Cj�µĺ������P(Cj|X).
     * 
     * @param x
     *            �ı���������
     * @param cj
     *            ���������
     * @return �������
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
            throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� [" + classifiedDir
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
                if(classification.equals(c)) { // ���������𣬺�ԭʼ������Ƿ���ͬ
                    correct++;
                }
            }
        }
        
        return ((double)correct/(double)total) * 100;
    }
    
 
    
    
    public static void test(NaiveBayesClassifier classifier, String[] args) {
    	long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��
        if (args[0].equals("-t")) { // ѵ��
            classifier.train(args[1], args[2]);
            System.out.println("ѵ�����");
        } else if(args[0].equals("-r")) { // ��ȡ��ȷ��
            double ret = classifier.getCorrectRate(args[1], args[2], args[3]);
            System.out.println("��ȷ��Ϊ��" + ret);
        } else { // ����
            classifier.loadModel(args[0]);

            String text = null;
            try {
                text = IntermediateData.getText(args[1], args[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            String result = classifier.classify(text); // ���з���

            System.out.println("������[" + result + "]");
        }

        long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
        System.out.println("��������ʱ�䣺 " + (endTime - startTime) + "ms");
    }
}
