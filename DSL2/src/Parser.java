import java.io.*;
import java.util.ArrayList;

public class Parser {
    private Step tempstep=new Step();
    private String lastStepid="";
    private Scrip scrip=new Scrip(); //语法树树根

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

    public void ParseFile(String filename){
        BufferedReader reader=null;
        File f=new File(filename);
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "gbk"));
            //加入编码字符集，解决中文乱码问题
            //reader = new BufferedReader(new FileReader(filename));
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
                //注释行情况
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
    public void ParseLine(String line){
        ArrayList<String>tokens=new ArrayList<>();
        //tempword获取这一行的每一个词法元素
        String tempword="";
        for(int i=0;i<line.length();i++)
        {
            char cur_char=line.charAt(i);
            if(cur_char == '#'){ //如果这一行中出现注释，忽略这一行接下来出现的所有符号
                break;
            }
            if(cur_char=='"')
            {
                //找到下一个双引号的位置，截取其双引号内字符串常量，赋值给tempword
                String sub=line.substring(i+1);
                int j=sub.indexOf('"');
                tempword=line.substring(i,j+i+1);
                //System.out.println("双引号内的为"+tempword);
                i+=j+1; //i移到下一个双引号之后
            }
            if (cur_char != ' ') {
                //没有遇见空格时，将遇见的字符一个一个拼凑成字符串
                tempword+=cur_char;
            }else{
                //遇见空格，字符串拼凑完毕，将此符号加入词法元素表中
                if(tempword!=""){
                    tokens.add(tempword);
                }
                tempword="";
            }
        }
        //将此行的最后一个字符串符号加入到词法元素表中
        tokens.add(tempword);
        //对这一行的词法元素进行处理
        ProcessTokens(tokens);
    }
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
    public void ProcessStep(String stepid){
        if(scrip.getEntry().equals("")){ //如果这是第一个step
            scrip.setEntry(stepid);
        }
        //对应每一段第一句话来存放
        if(!lastStepid.isEmpty()){ //如果lastStepid不是空，就把上一个step和上一个stepid对应起来，放进Name_Step表中
            scrip.getName_step().put(lastStepid,tempstep);
        }
        Step newstep=new Step();
        tempstep=newstep;
        lastStepid=stepid;
    }
    public void ProcessSpeak(ArrayList<String>tokens){
        ProcessExpression(tokens);
    }

    public boolean ProcessExpression(ArrayList<String>tokens){
        for(int i=0;i<tokens.size();i++){
            switch (tokens.get(i).charAt(0)){
                case '$':
                    //如果是变量就把变量名存在scrip的变量表中
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
    public void ProcessListen(int startTimer,int endTimer){
        Listen listen=new Listen(startTimer,endTimer);
        tempstep.setNeedListen(true);
        tempstep.setListen(listen);
    }
    public void ProcessBranch(String answer,String nextStepid){
        String var=answer.substring(1,answer.length()-1); //去掉answer的双引号
        tempstep.branches.put(var,nextStepid);
    }
    public void ProcessDefault(String nextStepid){
        tempstep.default_to=nextStepid;
    }
    public void ProcessExit(){
        tempstep.setExit(true);
    }
    public void ProcessSilence(String nextStepid){
        tempstep.silence_to=nextStepid;

    }
    public void test(){
        ParseFile("test.txt");
    }
}
