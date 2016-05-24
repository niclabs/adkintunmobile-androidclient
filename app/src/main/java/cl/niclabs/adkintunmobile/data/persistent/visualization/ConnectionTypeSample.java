package cl.niclabs.adkintunmobile.data.persistent.visualization;

import cl.niclabs.android.data.Persistent;

/**
 * Created by diego on 23-05-16.
 */
public class ConnectionTypeSample extends Persistent<ConnectionTypeSample> {

    protected int type;
    protected long initialTime;
    protected DailyConnectionTypeSummary date;


    public ConnectionTypeSample(){}

    public long getInitialTime(){
        return initialTime;
    }

    public int getType(){
        return type;
    }
}
