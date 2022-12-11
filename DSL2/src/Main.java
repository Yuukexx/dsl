
public class Main {
    public static void main(String[] args) {
        Interpreter inter=new Interpreter();
        User user=new User();
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
        inter.run(cfile);
    }
}
