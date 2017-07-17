import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class BernoulliNB extends NaiveBayesClassifier {
	protected void calculatePc() {
        for (int i = 0; i < db.classifications.length; i++) {
            model.setPc(i, (double)db.filesOfC[i] / (double)db.files);
        }
    }
    

	protected void calculatePxc() {
		for (int i = 0; i < db.classifications.length; i++) {
			HashMap<String, Integer> source =  db.filesOfXC[i];
			// HashMap<String, Double> target = model.pXC[i];
			
			for(Iterator<String> iter = db.vocabulary.iterator(); iter.hasNext();) {
				String t = iter.next();
				
				Integer value = source.get(t);
				if(value == null) { // 本类别下不包含单词t
					value = 0;
				}
				model.setPxc(t, i, (double)(value + 1)/(double)(db.filesOfC[i] + 2));
			}
		}
    }
    
	
	
    protected double calcProd(final String[] x, final int cj) {
    	double ret = 0.0;
    	HashSet<String> words = new HashSet<String>();
    	
    	for(String s:x) {
    		words.add(s);
    	}
    	
    	for(Iterator<String> iter = model.vocabulary.iterator(); iter.hasNext();) {
			String t = iter.next();
			
			if(words.contains(t)) {
				ret += Math.log(model.getPxc(t, cj));
			} else {
				ret += Math.log(1- model.getPxc(t, cj));
			}
    	}
    	
        // 再乘以先验概率
        ret += Math.log(model.getPc(cj));
        return ret;
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BernoulliNB nb = new BernoulliNB();
		//训练数据生成模型
		String[] arg={"-t","./data/train.db","./data/train.mdl"};
		MultiNomialNB.test(nb, arg);
		//使用模型对文件分类
//		String[] arg0={"./data/train.mdl","./data/temp.txt","gbk"};
//		MultiNomialNB.test(nb, arg0);
		//使用测试集评价训练的准确率
//		String[] arg2={"-r","./data/answer/","gbk","./data/train.mdl"};
//		MultiNomialNB.test(nb, arg2);
	}
}
