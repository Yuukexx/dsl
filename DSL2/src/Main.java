
public class Main {
    public static void main(String[] args) {
        Interpreter inter=new Interpreter();
        User user=new User();
        user.setName(user.processString(args[0]));
        user.setPhoneNum(user.processString(args[1]));
        user.setAmount(user.processString(args[2]));
        inter.setUser(user);
        inter.run("test.rsl");
        System.out.println(user);
        //Interpreter i = new Interpreter();
        //i.run();
    }
}
