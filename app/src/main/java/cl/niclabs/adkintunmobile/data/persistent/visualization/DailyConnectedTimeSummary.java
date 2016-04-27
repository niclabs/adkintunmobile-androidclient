package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.android.data.Persistent;

public class DailyConnectedTimeSummary extends Persistent<DailyConnectedTimeSummary> {

    public long date;

    public DailyConnectedTimeSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTimeInMillis();

    }


    public DailyConnectedTimeSummary(){}

    public Iterator<ConnectionTimeSample> getSamples(){
        String[] whereArgs = new String[1];
        whereArgs[0] =  Long.toString(getId());
        Iterator<ConnectionTimeSample> samples = find(ConnectionTimeSample.class, "date = ?", whereArgs, "initial_time");
        return samples;
    }

    public static DailyConnectedTimeSummary getSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String[] todayWhereArgs = new String[1];
        todayWhereArgs[0] = Long.toString(calendar.getTimeInMillis());

        long count = DailyConnectedTimeSummary.count(DailyConnectedTimeSummary.class, "date = ?", todayWhereArgs);

        if (count < 1){
            DailyConnectedTimeSummary todaySummary = new DailyConnectedTimeSummary(timestamp);
            todaySummary.save();
            return todaySummary;
        }
        return DailyConnectedTimeSummary.find(DailyConnectedTimeSummary.class, "date = ?", todayWhereArgs, "date").next();
    }

    public long getDateMillis(){
        return date;
    }
}