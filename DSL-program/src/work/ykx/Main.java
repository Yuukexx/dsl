package work.ykx;

public class Main {
    public static void main(String[] args) {
        User user=new User();
        user.setName(user.processString(args[0]));
        user.setPhoneNum(user.processString(args[1]));
        user.setAmount(Double.parseDouble(user.processString(args[2])));
        System.out.println(user);
        //Interpreter i = new Interpreter();
       //i.run();
    }
}
