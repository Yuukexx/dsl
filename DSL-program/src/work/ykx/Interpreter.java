package work.ykx;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
/**
 * 接通电话，连接媒体服务器，获取脚本语法树，创建执行环境
 * 当前step设为entrystep
 * 循环针对当前step做：
 *      执行Speak
 *      如果本步骤是终结步骤，则结束循环，断开通话
 *      执行Listen
 *      获得下一个stepId：如果用户沉默，则获得Silence的stepid
 *                      根据用户意向查找hashtable，获得stepid
 *                      如果查不到则获得default的stepid
 *       将当前Step置为刚才获得的stepid对应的step
 */
public class Interpreter {
    HashMap<String,String>varTable=new HashMap<>();
    Parser p=new Parser();
    Step curStep=new Step();
    HashMap<String,Train>trainMap=new HashMap<>();

    public void init(){
        trainMap=new HashMap<String,Train>();
        p=new Parser();
        FileInputStream fin= null;
        try {
            fin = new FileInputStream("db.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(fin,"GBK"));
            String line=reader.readLine();
            while(line!=null){
                String tempword="";
                ArrayList<String>temparray=new ArrayList<>();
                for(int i=0;i<line.length();i++){
                    char cur=line.charAt(i);
                    if (cur != ' ') {
                        tempword+=cur;
                    } else if (tempword != ""){
                        temparray.add(tempword);
                        tempword="";
                    }
                }
                temparray.add(tempword);
                if(temparray.size()==4) {
                    Train t = new Train(temparray.get(0), temparray.get(1), temparray.get(2), Integer.parseInt(temparray.get(3)));
                    temparray.clear();
                    trainMap.put(t.getStartCity(), t);
                }
                line=reader.readLine();
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        //init();
        p.ParseFile("test.rsl");
        curStep=p.scrip.getAns_step().get(p.scrip.getEntry());
        int repeat=0;
        String ans="";
        while(true){
            interExpression(curStep.expression);
            System.out.println();
            if(curStep.exit==1){
                break;
            }
            curStep.listen.run();

        }
    }
    public void interExpression(ArrayList<String>exps){
        for(int i=0;i<exps.size();i++){
            if(exps.get(i).charAt(0)=='"'){
                String var=exps.get(i).substring(1,exps.size());
                System.out.print(var);
            }else if(exps.get(i).charAt(0)=='$'){
                String var=exps.get(i).substring(1,exps.size()+1);
                if(varTable.get(var)==null){
                    System.out.println("Error!!Can't find variable!");
                }
                else{
                    System.out.print(varTable.get(var));
                }
            }
        }
    }
//    public void Listen(Listen listen){
//        try {
//            Thread.sleep(listen.getBeginTimer()*1000);
//            System.out.println("robot waiting");
//            int lastTime=listen.getEndTimer()- listen.getBeginTimer();
//            Thread.sleep(lastTime*1000);
//            System.out.println("time out");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }

}
