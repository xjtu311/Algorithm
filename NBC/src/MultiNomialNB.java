import java.util.HashMap;
import java.util.Iterator;

public class MultiNomialNB extends NaiveBayesClassifier {
	protected void calculatePc() {
        for (int i = 0; i < db.classifications.length; i++) {
            model.setPc(i, (double)db.tokensOfC[i] / (double)db.tokens);
        }
    }
    

	protected void calculatePxc() {
		for (int i = 0; i < db.classifications.length; i++) {
			HashMap<String, Integer> source =  db.tokensOfXC[i];
			//HashMap<String, Double> target = model.pXC[i];
			
			for(Iterator<String> iter = db.vocabulary.iterator(); iter.hasNext();) {
				String t = iter.next();
				
				Integer value = source.get(t);
				if(value == null) { // 本类别下不包含单词t
					value = 0;
				}
				model.setPxc(t, i, (double)(value + 1)/(double)(db.tokensOfC[i] + db.vocabulary.size()));
			}
		}
    }
    
	
	
    protected double calcProd(final String[] x, final int cj) {
        double ret = 0.0;
        // 类条件概率连乘
        for (int i = 0; i < x.length; i++) {
            // 因为结果过小，因此在连乘之前放大10倍，这对最终结果并无影响，因为我们只是比较概率大小而已
            ret += Math.log(model.getPxc(x[i], cj));
        }
        // 再乘以先验概率
        ret += Math.log(model.getPc(cj));
        return ret;
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MultiNomialNB nb = new MultiNomialNB();
		//训练数据生成模型
//		String[] arg={"-t","./data/train.db","./data/trainm.mdl"};
//		MultiNomialNB.test(nb, arg);
		//使用模型对文件分类
		String[] arg1={"./data/trainm.mdl","./data/temp.txt","gbk"};
		MultiNomialNB.test(nb, arg1);
		//使用测试集评价训练的准确率
		String[] arg2={"-r","./data/answer/","gbk","./data/trainm.mdl"};
		MultiNomialNB.test(nb, arg2);

	}

}
