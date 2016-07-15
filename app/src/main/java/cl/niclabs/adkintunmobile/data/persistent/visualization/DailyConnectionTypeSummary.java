package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.android.data.Persistent;

public abstract class DailyConnectionTypeSummary extends Persistent<DailyConnectionTypeSummary>{
    public long date;

    public DailyConnectionTypeSummary(){}

    public DailyConnectionTypeSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTimeInMillis();
    }

    public abstract Iterator<? extends ConnectionTypeSample> getSamples();

}
