package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.android.data.Persistent;

public class DailyConnectionTypeSummary extends Persistent<DailyConnectionTypeSummary> {

    public long date;

    public DailyConnectionTypeSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTimeInMillis();

    }


    public DailyConnectionTypeSummary(){}

    public Iterator<ConnectionTypeSample> getSamples(){
        String[] whereArgs = new String[1];
        whereArgs[0] =  Long.toString(getId());
        Iterator<ConnectionTypeSample> samples = find(ConnectionTypeSample.class, "date = ?", whereArgs, "initial_time");
        return samples;
    }

    public static DailyConnectionTypeSummary getSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String[] todayWhereArgs = new String[1];
        todayWhereArgs[0] = Long.toString(calendar.getTimeInMillis());

        long count = DailyConnectionTypeSummary.count(DailyConnectionTypeSummary.class, "date = ?", todayWhereArgs);

        if (count < 1){
            DailyConnectionTypeSummary todaySummary = new DailyConnectionTypeSummary(timestamp);
            todaySummary.save();
            return todaySummary;
        }
        return DailyConnectionTypeSummary.find(DailyConnectionTypeSummary.class, "date = ?", todayWhereArgs, "date").next();
    }

    public long getDateMillis(){
        return date;
    }
}