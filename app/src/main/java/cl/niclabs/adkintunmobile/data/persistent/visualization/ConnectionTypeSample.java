package cl.niclabs.adkintunmobile.data.persistent.visualization;

import cl.niclabs.android.data.Persistent;

public class ConnectionTypeSample extends Persistent<ConnectionTypeSample> {
    public void setType(int type) {
        this.type = type;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public DailyConnectionTypeSummary getDate() {
        return date;
    }

    public void setDate(DailyConnectionTypeSummary date) {
        this.date = date;
    }

    protected int type;
    protected long initialTime;
    protected DailyConnectionTypeSummary date;

    public long getInitialTime(){
        return initialTime;
    }

    public int getType(){
        return type;
    }

    public ConnectionTypeSample(){}
}
