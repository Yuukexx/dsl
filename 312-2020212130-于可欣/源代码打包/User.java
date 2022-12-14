
/**
 * 用于接收命令行参数
 * 考虑参数的扩展性，将参数封装到一个类里面
 */
public class User {
    private String name;
    private String phoneNum;
    private String amount;
    public String processString(String s){
        int index=s.indexOf("=");
        return s.substring(index+1);
    }
    public String toString(){
        return "name="+name+";phoneNum="+phoneNum+";amount="+amount+";";
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
