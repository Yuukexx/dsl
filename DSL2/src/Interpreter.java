import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

public class Interpreter {
    private HashMap<String,String>var_value=new HashMap<>(); //key=变量名，value=变量值
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
        //将语法树中的变量名和命令行参数对应起来
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
        int bufferOffset = 0;    //读取数据buf偏移量
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;//计算过期时间

        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < buf.length) { //时间到，buf被写满，或者到读取到内容时
            int readLength = Math.min(is.available(), buf.length - bufferOffset); //按可读数据长与buf长度，选择读取长度

            int readResult = is.read(buf, bufferOffset, readLength);

            if (readResult == -1) {//流结束直接结束
                break;
            }
            bufferOffset += readResult;
            if (readResult > 0) {   //读取到内容结束循环
                break;
            }
            try {
                Thread.sleep(10);          //等待10ms读取，减小cpu占用
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bufferOffset;
    }
    public String readInput() {

        int lastTime= curStep.listen.getEndTimer()- curStep.listen.getBeginTimer();
        byte[] inputData = new byte[100];//设置为1,只读第一个字符
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
    //把exp中的语句输出给用户
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
