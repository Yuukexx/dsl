package work.ykx;

public class Listen {
    private int beginTimer;
    private int endTimer;
    public Listen(){}
    public Listen(int beginTimer,int endTimer){
        this.beginTimer=beginTimer;
        this.endTimer=endTimer;
    }
    public void setBeginTimer(int beginTimer){
        this.beginTimer=beginTimer;
    }

    public void setEndTimer(int endTimer) {
        this.endTimer = endTimer;
    }

    public int getBeginTimer() {
        return beginTimer;
    }

    public int getEndTimer() {
        return endTimer;
    }

}
