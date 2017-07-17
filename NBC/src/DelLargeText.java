import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
 
public class DelLargeText {
    public void delSmallText(File srcFile){
        if(srcFile.isDirectory()){
            File[] childFiles=srcFile.listFiles();
            for(File child:childFiles){
                delSmallText(child);
            }
        }
        else if(srcFile.isFile()){
            StringBuffer content=new StringBuffer();
            try{
                FileReader fr=new FileReader(srcFile);
                BufferedReader br=new BufferedReader(fr);
                String line;
                while((line=br.readLine())!=null){      //readLine()������ȡĩβ�Ļ��з�
                    content.append(line.trim());    //ֻȥ������β�Ŀո���Ϊȥ���ո�Ļ�������Ӣ�ĵ��ʺϲ�����һ��
                }
                br.close();
                if(content.toString().length()>4000){
                    srcFile.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
     
    public static void main(String[] args){
        DelLargeText inst=new DelLargeText();
        File file=new File("./data/answer");
        inst.delSmallText(file);
        File file1=new File("./data/train");
        inst.delSmallText(file1);
    }
}