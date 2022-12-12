
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 看做语法树的根节点，存储所有的step，变量表并指定入口step
 */
public class Scrip {
    private HashMap<String,Step> name_step =new HashMap<String,Step>(); //存放所有的step, key=step名，value=step
    private ArrayList<String>vars=new ArrayList<>();  //变量表，存所有的变量名
    private String entry=""; //入口step名字

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
