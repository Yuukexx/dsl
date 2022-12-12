import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 解释器类，负责解释程序，模拟接通电话，连接至媒体服务器操作
 * 获取脚本语法树，创建执行环境
 */
public class Interpreter {
    private HashMap<String,String>var_value=new HashMap<>(); //变量表 key=变量名，value=变量值
    private User user=new User();
    private Step curStep =new Step(); //当前步骤
    private String curStepName=""; //当前步骤名
    private Parser parser=new Parser();  //存储语法树
    private Log log=new Log(); //输出日志对象

    /**
     * 设置用户信息
     * @param user 用户对象
     */
    public void setUser(User user) {
        this.user = user;
        var_value.put("amount",user.getAmount());
        log.write("USER LOGIN: "+user);
    }

    /**
     * 将语法树中的变量名和命令行参数对应起来
     */
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

    /**
     *解释器执行的代码主体，获取脚本语法树，创建执行环境
     * 进行step的执行和跳转
     * 对listen和speak操作进行处理
     * @param conf_file 脚本文件
     */
    public void run(String conf_file){
        System.out.println("==========CONVERSATION BEGIN===========");
        parser.ParseFile(conf_file);
        String entrance=parser.getScrip().getEntry();
        curStep = parser.getScrip().getName_step().get(entrance);
        String answer="";
        boolean answerFlag=false; //表示是否监听到了用户回答
        while(true){
            answerFlag=false;
            if(curStep.expression!=null){
                DOS.robotOut();
                speak(curStep.expression);
            }
            if(curStep.IsExit()){
                System.out.println("==========CONVERSATION END=============");
                log.write(user.getName()+":LOGOUT");
                break;
            }
            if(curStep.IsNeedListen()){
                DOS.userOut();
                answer=listen();
                answerFlag=true;
                answer=answer.replaceAll("\r|\n", "");
            }
            if(answer.isEmpty()){
                System.out.println();
                log.write(user.getName()+":recieve no input");
                if(parser.getScrip().getName_step().get(curStep.silence_to)==null){
                    curStep=parser.getScrip().getName_step().get(curStep.default_to);
                }else{
                    curStep=parser.getScrip().getName_step().get(curStep.silence_to);
                }

            }
            else if(curStep.getBranches().get(answer)==null){
                if(answerFlag)
                log.write(user.getName()+":GET THE ANSWER "+answer);
                curStep=parser.getScrip().getName_step().get(curStep.default_to);

            }else{
                if(answerFlag)
                log.write(user.getName()+":GET THE ANSWER "+answer);
                curStepName=curStep.getBranches().get(answer);
                curStep=parser.getScrip().getName_step().get(curStep.getBranches().get(answer));
                if(curStep==null){
                    System.out.println("Error!!Can't find the current step!");
                    log.write(user.getName()+":Error!!Can't find the current step!");
                }
            }
        }
    }

    /**
     * 获取用户输入
     * @return 用户输入的字符串
     */
    public String getInput(){
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        return str;
    }

    /**
     *实现对话的超时退出功能
     * @param is 输入流
     * @param buf 初始缓冲区数组
     * @param timeoutMillis 最大等待时间
     * @return 读取的字符长度
     * @throws IOException 捕获IO异常
     */
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

    /**
     * 从控制台获取输入操作
     * @return 用户输入的字符串
     */
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
    /**
     * 模拟调用媒体服务器对客户说的话录音，并进行语音识别，语音识别的结果调用自然语言分析服务分析客户的意愿
     * @return 机器人听到的语句
     */
    public String listen() {
        return readInput();
    }

    /**
     * 模拟机器人语音合成操作
     * 计算表达式合成一段文字，模拟调用媒体服务器进行语音合成并播放
     * @param exps 需要计算的表达式内容
     */
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

    /**
     * 媒体服务器测试桩
     * 将机器人回复内容输出到文件中
     * @param exps 需要输出的表达式
     * @param ofile 输出文件
     */
    public void testSpeak(ArrayList<String>exps,File ofile){
        Writer out=null;
        try{
            out=new FileWriter(ofile,true);

            for(int i=0;i<exps.size();i++){
                if(exps.get(i).charAt(0)=='"'){
                    String var=exps.get(i).substring(1,exps.get(i).length()-1);
                    out.write(var);
                }else if(exps.get(i).charAt(0)=='$'){
                    String var=exps.get(i).substring(1);
                    if(var_value.get(var)==null){
                        out.write("Error!!Can't find variable!\n");
                    }
                    else{
                        out.write(var_value.get(var));
                    }
                }
            }
            out.write("\n");
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 匹配两个文件的差异，如果有差异输出差异地方
     * @param validFile 预期输出文件
     * @param testFile 实际输出文件
     * @throws IOException 抛出IO异常
     */
    public static boolean FileIsEqual(String validFile, String testFile) throws IOException {
        List<String> list1 =  Files.readAllLines(Paths.get(validFile));
        List<String> list2 =  Files.readAllLines(Paths.get(testFile));
        List<String> finalList = list2.stream().filter(line ->
                list1.stream().filter(line2 -> line2.equals(line)).count() == 0
        ).collect(Collectors.toList());
        if (finalList.size() == 0) {
            return true;
        }else{
            finalList.forEach(one -> System.out.println(one));
            return false;
        }
    }

    /**
     * 解释器的测试主函数
     * 自然语言分析测试桩
     * @param conf_file 脚本文件
     * @param in_file 测试文件
     */
    public void test(String conf_file,String in_file){
        parser.ParseFile(conf_file);
        String entrance=parser.getScrip().getEntry();
        curStep = parser.getScrip().getName_step().get(entrance);
        File file=new File("output"+in_file.substring(5));
        try {
            FileWriter fileWriter=new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String answer="";
        BufferedReader reader=null;
        ArrayList<String>answers=new ArrayList<>();
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(in_file), "gbk"));
            String line=reader.readLine();
            String content;
            while(line!=null){
                //删除行首空白
                content=line.trim();
                //空行情况
                if(content.length()==0){
                    line=reader.readLine();
                    continue;
                }
                answers.add(content);
                line=reader.readLine();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i=0;
        while(true) {
            answer="";
            if (curStep.expression != null) {
                testSpeak(curStep.expression,file);
            }
            if (curStep.IsExit()) {
                log.write(user.getName() + ":LOGOUT");
                break;
            }
            if (curStep.IsNeedListen()) {
                if(i<answers.size()) {
                    answer = answers.get(i);
                    i++;
                }
                answer = answer.replaceAll("\r|\n", "");
            }
            if (answer.isEmpty()) {
                log.write(user.getName() + ":recieve no input");
                if (parser.getScrip().getName_step().get(curStep.silence_to) == null) {
                    curStep = parser.getScrip().getName_step().get(curStep.default_to);
                } else {
                    curStep = parser.getScrip().getName_step().get(curStep.silence_to);
                }

            } else if (curStep.getBranches().get(answer) == null) {
                log.write(user.getName() + ":GET THE ANSWER " + answer);
                curStep = parser.getScrip().getName_step().get(curStep.default_to);

            } else {
                log.write(user.getName() + ":GET THE ANSWER " + answer);
                curStepName = curStep.getBranches().get(answer);
                curStep = parser.getScrip().getName_step().get(curStep.getBranches().get(answer));
                if (curStep == null) {
                    System.out.println("Error!!Can't find the current step!");
                    log.write(user.getName() + ":Error!!Can't find the current step!");
                }
            }
        }
        String validPath="valid"+in_file.substring(5);
        String testPath= String.valueOf(file);
        try {
            if(FileIsEqual(validPath,testPath)){
                System.out.println("Test passed between "+testPath+" and "+validPath+"!Congratulations!");
            }else{
                System.out.println("The output between "+testPath+" and "+validPath+ " is unexpected. Your program is best! Only need to fix some bugs. ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
