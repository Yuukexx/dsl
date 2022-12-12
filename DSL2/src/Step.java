
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 步骤类
 * 完整表示一个步骤所有行为
 */
public class Step {
    ArrayList<String>expression=new ArrayList<>(); //记录Speak的表达式
    Map<String,String> branches =new HashMap<>(); //key=用户需求的名字, value=用户需求对应分支的StepId,对应一个step中的多个Branch分支
    Listen listen=new Listen();
    String silence_to=new String();  //沉默跳转步骤名
    String default_to=new String();  //默认跳转步骤名
    private boolean exit=false;   //是否需要退出
    private boolean needListen=false;  //是否需要监听操作

    public void setNeedListen(boolean needListen) {
        this.needListen = needListen;
    }
    public boolean IsNeedListen(){
        return needListen;
    }

    public boolean IsExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void setBranches(Map<String, String> branches) {
        this.branches = branches;
    }

    public void setDefault_to(String default_to) {
        this.default_to = default_to;
    }

    public void setExpression(ArrayList<String> expression) {
        this.expression = expression;
    }

    public void setListen(Listen listen) {
        this.listen = listen;
    }

    public void setSilence_to(String silence_to) {
        this.silence_to = silence_to;
    }

    public Listen getListen() {
        return listen;
    }

    public String getDefault_to() {
        return default_to;
    }

    public String getSilence_to() {
        return silence_to;
    }

    public Map<String, String> getBranches() {
        return branches;
    }

    public ArrayList<String> getExpression() {
        return expression;
    }
}
