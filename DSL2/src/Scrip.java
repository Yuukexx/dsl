
import java.util.ArrayList;
import java.util.HashMap;

/**
 * �����﷨���ĸ��ڵ㣬�洢���е�step��������ָ�����step
 */
public class Scrip {
    private HashMap<String,Step> name_step =new HashMap<String,Step>(); //������е�step, key=step����value=step
    private ArrayList<String>vars=new ArrayList<>();  //�����������еı�����
    private String entry=""; //���step����

    public void setName_step(HashMap<String, Step> name_step) {
        this.name_step = name_step;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setVars(ArrayList<String> vars) {
        this.vars = vars;
    }

    public String getEntry() {
        return entry;
    }

    public ArrayList<String> getVars() {
        return vars;
    }

    public HashMap<String, Step> getName_step() {
        return name_step;
    }
}
