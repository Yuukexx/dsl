package work.ykx;

public class Train {
    private String departureTime;
    private String startCity;
    private String endCity;
    private int ticketNum;
    public Train(){}
    public Train(String departureTime,String startCity,String endCity,int ticketNum){
        this.departureTime=departureTime;
        this.endCity=endCity;
        this.startCity=startCity;
        this.ticketNum=ticketNum;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getEndCity() {
        return endCity;
    }

    public String getStartCity() {
        return startCity;
    }
}
