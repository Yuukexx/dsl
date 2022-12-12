import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志管理模块
 */
public class Log {
    /**
     * 将指定内容写入日志文件
     * @param s 需要写入日志的内容
     */
    public static void write(String s){
        String filepath="log.txt";
        Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(date);
        File file=new File(filepath);
        Writer out=null;
        try{
            out=new FileWriter(file,true);
            out.write("\n["+time+"]:"+s);
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
