import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

public class Interpreter {
    private HashMap<String,String>var_value=new HashMap<>(); //key=��������value=����ֵ
    private User user=new User();
    private DOS dos=new DOS();
    private Step curStep =new Step();
    private String curStepName="";
    private Parser parser=new Parser();

    public void setUser(User user) {
        this.user = user;
        var_value.put("amount",user.getAmount());
    }

    public void setArgs(){
        //���﷨���еı������������в�����Ӧ����
        for(int i=0;i<parser.getScrip().getVars().size();i++)
        {
            String var=parser.getScrip().getVars().get(i);
            if(var.equals("amount")){
                var_value.put(var,user.getAmount());
            }
        }
    }
    public void run(String conf_file){
        parser.ParseFile(conf_file);
        String entrance=parser.getScrip().getEntry();
        curStep = parser.getScrip().getName_step().get(entrance);
        String answer="";
        while(true){
            if(curStep.expression!=null){
                dos.robotOut();
                speak(curStep.expression);
            }
            if(curStep.IsExit()){
                System.exit(0);
            }
            if(curStep.IsNeedListen()){
                dos.userOut();
                answer=listen();
                answer=answer.replaceAll("\r|\n", "");
            }
            if(answer.isEmpty()){
                System.out.println();
                curStep=parser.getScrip().getName_step().get(curStep.silence_to);
            }
            else if(curStep.getBranches().get(answer)==null){
                //dos.robotOut();
               // System.out.println("Sorry, I can't understand you. I will be better in the future :)");
                curStep=parser.getScrip().getName_step().get(curStep.default_to);
            }else{
                curStepName=curStep.getBranches().get(answer);
                curStep=parser.getScrip().getName_step().get(curStep.getBranches().get(answer));
                if(curStep==null){
                    System.out.println("Error!!Can't find the current step!");
                }
            }
//            if(parser.getScrip().getName_step().get(answer)==null){
//                curStep=parser.getScrip().getName_step().get(parser.getTempstep().default_to);
//            }else{
//                curStep=parser.getScrip().getName_step().get(answer);
//            }
        }
    }
    public String getInput(){
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        return str;
    }

    public int readInputStreamWithTimeout(InputStream is, byte[] buf, int timeoutMillis)
            throws IOException {
        int bufferOffset = 0;    //��ȡ����bufƫ����
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;//�������ʱ��

        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < buf.length) { //ʱ�䵽��buf��д�������ߵ���ȡ������ʱ
            int readLength = Math.min(is.available(), buf.length - bufferOffset); //���ɶ����ݳ���buf���ȣ�ѡ���ȡ����

            int readResult = is.read(buf, bufferOffset, readLength);

            if (readResult == -1) {//������ֱ�ӽ���
                break;
            }
            bufferOffset += readResult;
            if (readResult > 0) {   //��ȡ�����ݽ���ѭ��
                break;
            }
            try {
                Thread.sleep(10);          //�ȴ�10ms��ȡ����Сcpuռ��
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bufferOffset;
    }
    public String readInput() {

        int lastTime= curStep.listen.getEndTimer()- curStep.listen.getBeginTimer();
        byte[] inputData = new byte[100];//����Ϊ1,ֻ����һ���ַ�
        int readLength = 0;
        try {
            readLength = readInputStreamWithTimeout(System.in, inputData,lastTime*1000 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = "";
        for (byte b: inputData) {
            if (b != 0) {
                s += (char)b;
            }
        }
        return s;
    }

    public String listen() {
        return readInput();
    }
    //��exp�е����������û�
    public void speak(ArrayList<String>exps){
        for(int i=0;i<exps.size();i++){
            if(exps.get(i).charAt(0)=='"'){
                String var=exps.get(i).substring(1,exps.get(i).length()-1);
                System.out.print(var);
            }else if(exps.get(i).charAt(0)=='$'){
                String var=exps.get(i).substring(1);
                if(var_value.get(var)==null){
                    System.out.println("Error!!Can't find variable!");
                }
                else{
                    System.out.print(var_value.get(var));
                }
            }
        }
        System.out.println();
    }
}
