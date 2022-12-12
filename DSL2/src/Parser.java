import java.io.*;
import java.util.ArrayList;

/**
 * �﷨����ģ�飬�Խű������﷨����
 */
public class Parser {
    private Step tempstep=new Step(); //��ǰ����
    private String lastStepid=""; //�洢��һ���Ĳ�����
    private Scrip scrip=new Scrip(); //�﷨������

    public void setLastStepid(String lastStepid) {
        this.lastStepid = lastStepid;
    }

    public void setScrip(Scrip scrip) {
        this.scrip = scrip;
    }

    public void setTempstep(Step tempstep) {
        this.tempstep = tempstep;
    }

    public Scrip getScrip() {
        return scrip;
    }

    public Step getTempstep() {
        return tempstep;
    }

    public String getLastStepid() {
        return lastStepid;
    }

    /**
     * ���ļ������ļ���ÿһ�н��ж�ȡ������ÿһ�еĴ������
     * @param filename �ű��ļ���
     */
    public void ParseFile(String filename){
        BufferedReader reader=null;
        File f=new File(filename);
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "gbk"));
            //��������ַ��������������������
            String line=reader.readLine();
            String content;
            while(line!=null){
                //ɾ�����׿հ�
                content=line.trim();
                //�������
                if(content.length()==0){
                    line=reader.readLine();
                    continue;
                }
                //ע�������
                else if(content.startsWith("#")){
                    line=reader.readLine();
                    continue;
                }
                ParseLine(content);
                line=reader.readLine();
            }
            scrip.getName_step().put(lastStepid,tempstep);
        }catch(FileNotFoundException e){
            System.out.println("Error!!File is not found!");
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(null!=reader){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ���ļ���ÿһ�н��д�����ȡÿһ��token����
     * @param line ����һ�е������ַ����ַ���
     */
    public void ParseLine(String line){
        ArrayList<String>tokens=new ArrayList<>();
        //tempword��ȡ��һ�е�ÿһ���ʷ�Ԫ��
        String tempword="";
        for(int i=0;i<line.length();i++)
        {
            char cur_char=line.charAt(i);
            if(cur_char == '#'){ //�����һ���г���ע�ͣ�������һ�н��������ֵ����з���
                break;
            }
            if(cur_char=='"')
            {
                //�ҵ���һ��˫���ŵ�λ�ã���ȡ��˫�������ַ�����������ֵ��tempword
                String sub=line.substring(i+1);
                int j=sub.indexOf('"');
                tempword=line.substring(i,j+i+1);
                i+=j+1; //i�Ƶ���һ��˫����֮��
            }
            if (cur_char != ' ') {
                //û�������ո�ʱ�����������ַ�һ��һ��ƴ�ճ��ַ���
                tempword+=cur_char;
            }else{
                //�����ո��ַ���ƴ����ϣ����˷��ż���ʷ�Ԫ�ر���
                if(tempword!=""){
                    tokens.add(tempword);
                }
                tempword="";
            }
        }
        //�����е����һ���ַ������ż��뵽�ʷ�Ԫ�ر���
        tokens.add(tempword);
        //����һ�еĴʷ�Ԫ�ؽ��д���
        ProcessTokens(tokens);
    }

    /**
     * ��ÿһ��token����
     * @param tokens token�б�
     */
    public void ProcessTokens(ArrayList<String>tokens){
        String token0=tokens.get(0);
        if(token0.equals("Step")){
            ProcessStep(tokens.get(1));
        }
        else if(token0.equals("Speak")){
            ArrayList<String>var=new ArrayList<String>();
            for(int i=1;i<tokens.size();i++){
                var.add(tokens.get(i));
            }
            ProcessSpeak(var);
        }
        else if(token0.equals("Listen")){
            ProcessListen(Integer.parseInt(tokens.get(1)),Integer.parseInt(tokens.get(2)));
        }
        else if(token0.equals("Branch")){
            ProcessBranch(tokens.get(1),tokens.get(2));
        }
        else if(token0.equals("Silence")){
            ProcessSilence(tokens.get(1));
        }
        else if(token0.equals("Default")){
            ProcessDefault(tokens.get(1));
        }
        else if(token0.equals("Exit")){
            ProcessExit();
        }
        else{
            System.out.println("Error!!Unknown string!");
        }
    }

    /**
     * �����ַ���Step���д���
     * @param stepid ��ǰ��������
     */
    public void ProcessStep(String stepid){
        if(scrip.getEntry().equals("")){ //������ǵ�һ��step
            scrip.setEntry(stepid);
        }
        //��Ӧÿһ�ε�һ�仰�����
        if(!lastStepid.isEmpty()){ //���lastStepid���ǿգ��Ͱ���һ��step����һ��stepid��Ӧ�������Ž�Name_Step����
            scrip.getName_step().put(lastStepid,tempstep);
        }
        Step newstep=new Step();
        tempstep=newstep;
        lastStepid=stepid;
    }

    /**
     * �����ַ���Speak���д���
     * @param tokens
     */
    public void ProcessSpeak(ArrayList<String>tokens){
        ProcessExpression(tokens);
    }

    /**
     * �����ʽ���뵱ǰ��step
     * @param tokens ���ʽ�﷨Ԫ���б�
     * @return �Ƿ����ɹ�
     */
    public boolean ProcessExpression(ArrayList<String>tokens){
        for(int i=0;i<tokens.size();i++){
            switch (tokens.get(i).charAt(0)){
                case '$':
                    //����Ǳ����Ͱѱ���������scrip�ı�������
                    scrip.getVars().add(tokens.get(i).substring(1));
                    tempstep.expression.add(tokens.get(i));
                    break;
                case '"':
                    tempstep.expression.add(tokens.get(i));
                    break;
                case '+':
                    continue;
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * �����ַ�����Listen"���д���
     * @param startTimer ������ʼʱ��
     * @param endTimer ��������ʱ��
     */
    public void ProcessListen(int startTimer,int endTimer){
        Listen listen=new Listen(startTimer,endTimer);
        tempstep.setNeedListen(true);
        tempstep.setListen(listen);
    }

    /**
     * �����ַ�����Branch"���д���
     * @param answer �û��Ļش�
     * @param nextStepid ��һ������
     */
    public void ProcessBranch(String answer,String nextStepid){
        String var=answer.substring(1,answer.length()-1); //ȥ��answer��˫����
        tempstep.branches.put(var,nextStepid);
    }

    /**
     * �����ַ���Default���д���
     * @param nextStepid ��һ�����������
     */
    public void ProcessDefault(String nextStepid){
        tempstep.default_to=nextStepid;
    }

    /**
     * �˳�����
     */
    public void ProcessExit(){
        tempstep.setExit(true);
    }

    /**
     * ��Ĭ���̴���
     * @param nextStepid ��һ����������
     */
    public void ProcessSilence(String nextStepid){
        tempstep.silence_to=nextStepid;

    }

    /**
     * ��̹����еĲ���׮����ʽ�����в�����
     */
    public void test(){
        ParseFile("test.txt");
    }
}
