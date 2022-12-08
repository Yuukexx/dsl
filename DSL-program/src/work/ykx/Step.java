package work.ykx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Step {
    ArrayList<String>expression=new ArrayList<>();
    Map<String,String>answer_step=new HashMap<>();
    Listen listen=new Listen();
    String silence_to=new String();
    String default_to=new String();
    int exit=0;
    public void setAnswer_step(Map<String, String> answer_step) {
        this.answer_step = answer_step;
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

    public Map<String, String> getAnswer_step() {
        return answer_step;
    }

    public ArrayList<String> getExpression() {
        return expression;
    }
}
