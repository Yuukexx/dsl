package work.ykx;

/**
 * ���ڽ��������в���
 * ���ǲ�������չ�ԣ���������װ��һ��������
 */
public class User {
    private String name;
    private String phoneNum;
    private double amount;
    public String processString(String s){
        int index=s.indexOf("=");
        return s.substring(index+1);
    }
    public String toString(){
        return "name="+name+";phoneNum="+phoneNum+";amount="+amount+";";
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
