/**
 * @author yukexin-312-2020212130
 * @version 1.0
 */
public class Main {
    static Interpreter inter=new Interpreter();
    static User user=new User();
    /**
     * 接收并处理命令行参数，启动程序
     * @param args 控制台输入参数，形式为 -d test.rsl -i name=Mr.Zhang phonenum=18842901424 amount=100.00
     */
    public static void main(String[] args) {

        //-d test.rsl -i name=Mr.Zhang phonenum=18842901424 amount=100.00
        String fp="",cfile="";
        for(int i=0;i<args.length;i++){
            fp=args[i];
            switch (fp){
                case "-d":
                    cfile=args[1];
                    i++;
                    break;
                case "-i":
                    user.setName(user.processString(args[i+1]));
                    user.setPhoneNum(user.processString(args[i+2]));
                    user.setAmount(user.processString(args[i+3]));
                    inter.setUser(user);
                    i+=3;
                    break;
                default:
                    System.out.println("Error!!Wrong pattern of arguments!");
            }
        }
        //inter.run(cfile);
        test(cfile);
    }
    public static void test(String cfile){
        inter.test(cfile,"input1.txt");
        inter.test(cfile,"input2.txt");
        inter.test(cfile,"input3.txt");
        inter.test(cfile,"input4.txt");
        inter.test(cfile,"input5.txt");
    }
}
