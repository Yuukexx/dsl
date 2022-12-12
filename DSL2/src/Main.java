import java.io.*;

/**
 * @author yukexin-312-2020212130
 * @version 1.0
 */
public class Main {
    static Interpreter inter=new Interpreter();
    static User user=new User();
    /**
     * ���ղ����������в�������������
     * @param args ����̨���������
     *             ����������ʽΪ -d test.rsl -i name=Mr.Zhang phonenum=18842901424 amount=100.00
     *             ������ʽΪ -d test.rsl -t testfile.txt -i name=Mr.Zhang phonenum=18842901424 amount=100.00
     */
    public static void main(String[] args) {

        //-d test.rsl -i name=Mr.Zhang phonenum=18842901424 amount=100.00
        //-d test.rsl -t testfile.txt -i name=Mr.Zhang phonenum=18842901424 amount=100.00
        String fp="",cfile="",tfile="";
        Boolean isRun=false;
        Boolean isTest=false;
        for(int i=0;i<args.length;i++){
            fp=args[i];
            switch (fp){
                case "-d":
                    cfile=args[i+1];
                    i++;
                    break;
                case "-i":
                    user.setName(user.processString(args[i+1]));
                    user.setPhoneNum(user.processString(args[i+2]));
                    user.setAmount(user.processString(args[i+3]));
                    inter.setUser(user);
                    isRun=true;
                    i+=3;
                    break;
                case "-t":
                    tfile=args[i+1];
                    i++;
                    isTest=true;
                    break;
                default:
                    System.out.println("Error!!Wrong pattern of arguments!");
            }
        }

        if(isTest) {
            test(cfile, tfile);
        }else if(isRun) {
            inter.run(cfile);
        }

    }

    /**
     * �����Զ����Խű���ʵ�ֽ�����Զ��ȶ�
     * @param cfile �ű��ļ�
     * @param tfile �Զ����Խű����������в����ļ����в���
     */
    public static void test(String cfile,String tfile){
        try {
            FileInputStream in=new FileInputStream(tfile);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
            String strfile=null;
            while((strfile=bufferedReader.readLine())!=null)
            {
                inter.test(cfile,strfile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
