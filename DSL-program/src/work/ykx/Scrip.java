package work.ykx;

import java.util.ArrayList;
import java.util.HashMap;

public class Scrip {
    private HashMap<String,Step> ans_step =new HashMap<String,Step>(); //key=用户的回答，step=对应的分支step
    private ArrayList<String>vars=new ArrayList<>();
    private String entry="";

    public void setAns_step(HashMap<String, Step> ans_step) {
        this.ans_step = ans_step;
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

    public HashMap<String, Step> getAns_step() {
        return ans_step;
    }
}
