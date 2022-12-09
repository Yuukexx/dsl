package work.ykx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Step {
    ArrayList<String>expression=new ArrayList<>(); //记录Speak的表达式
    Map<String,String> branches =new HashMap<>(); //key=用户需求的名字, value=用户需求对应分支的StepId
    Listen listen=new Listen();
    String silence_to=new String();
    String default_to=new String();
    int exit=0;
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
