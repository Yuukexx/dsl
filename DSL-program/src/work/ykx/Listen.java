package work.ykx;

public class Listen {
    private int beginTimer;
    private int endTimer;
    public Listen(){}
    public Listen(int beginTimer,int endTimer){
        this.beginTimer=beginTimer;
        this.endTimer=endTimer;
    }
    public void run(){
        try {
            //读不到就沉睡醒了再读
            Thread.sleep(this.getBeginTimer()*1000);
            int lastTime=this.getEndTimer()- this.getBeginTimer();
            System.out.println("I will waiting for "+lastTime+" s.");
            Thread.sleep(lastTime*1000);
            System.out.println("time out");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
