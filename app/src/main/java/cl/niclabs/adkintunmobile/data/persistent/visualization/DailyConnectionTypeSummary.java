package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Iterator;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.commons.data.Persistent;

public abstract class DailyConnectionTypeSummary extends Persistent<DailyConnectionTypeSummary>{
    public long date;

    public DailyConnectionTypeSummary(){}

    public DailyConnectionTypeSummary(long timestamp){
        date = DisplayDateManager.timestampAtStartDay(timestamp);
    }

    public abstract Iterator<? extends ConnectionTypeSample> getSamples();

}
