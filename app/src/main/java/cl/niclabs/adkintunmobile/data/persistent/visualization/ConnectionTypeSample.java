package cl.niclabs.adkintunmobile.data.persistent.visualization;

import cl.niclabs.android.data.Persistent;

public class ConnectionTypeSample extends Persistent<ConnectionTypeSample> {

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
