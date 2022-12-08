package work.ykx;
import java.io.*;
import java.util.ArrayList;

public class Parser {
    Step tempstep=new Step();
    String lastStepid="";
    Scrip scrip=new Scrip();
    public void ParseFile(String filename){
        BufferedReader reader=null;
        File f=new File(filename);
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "gbk"));
            //加入编码字符集，解决中文乱码问题
            //reader = new BufferedReader(new FileReader(filename));
            String line=reader.readLine();
            while(!line.trim().startsWith("End")){
                //Thread.sleep(1000);
                String content=line.trim();
                //空行情况
                if(content.length()==0){
                    line=reader.readLine();
                    continue;
                }
                else if(content.startsWith("#")){
                    line=reader.readLine();
                    continue;
                }
                System.out.println(line);
                ParseLine(line);
                line=reader.readLine();
            }
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
        String tempword="";
        for(int i=0;i<line.length();i++)
        {
            char cur_char=line.charAt(i);
            if(cur_char == '#'){
                break;
            }
            if (cur_char != ' ') {
                tempword+=cur_char;
            }else{
                if(tempword!=""){
                    tokens.add(tempword);
                }
                tempword="";
            }
        }
        tokens.add(tempword);
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
            //ProcessSilence(tokens.get(1));
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
        if(!lastStepid.isEmpty()){
            scrip.steps.put(lastStepid,tempstep);
        }
        Step newstep=new Step();
        tempstep=newstep;
        lastStepid=stepid;
        if(scrip.steps.size()==0){
            scrip.entry=stepid;
        }
    }
    public void ProcessSpeak(ArrayList<String>tokens){
        ProcessExpression(tokens);
    }

    public boolean ProcessExpression(ArrayList<String>tokens){
        for(int i=0;i<tokens.size();i++){
            switch (tokens.get(i).charAt(0)){
                case '$':
                    scrip.vars.add(tokens.get(i).substring(1));
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
        tempstep.setListen(listen);
    }
    public void ProcessBranch(String answer,String nextStepid){
        String var=answer.substring(1,answer.length());
        tempstep.answer_step.put(var,nextStepid);
    }
    public void ProcessDefault(String nextStepid){
        tempstep.default_to=nextStepid;
    }
    public void ProcessExit(){
        tempstep.exit=1;
    }
    public void test(){
        ParseFile("test.txt");
    }
}
