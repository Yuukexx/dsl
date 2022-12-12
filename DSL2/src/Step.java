
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ������
 * ������ʾһ������������Ϊ
 */
public class Step {
    ArrayList<String>expression=new ArrayList<>(); //��¼Speak�ı��ʽ
    Map<String,String> branches =new HashMap<>(); //key=�û����������, value=�û������Ӧ��֧��StepId,��Ӧһ��step�еĶ��Branch��֧
    Listen listen=new Listen();
    String silence_to=new String();  //��Ĭ��ת������
    String default_to=new String();  //Ĭ����ת������
    private boolean exit=false;   //�Ƿ���Ҫ�˳�
    private boolean needListen=false;  //�Ƿ���Ҫ��������

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
