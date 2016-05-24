package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class DailyConnectionModeSummary extends DailyConnectionTypeSummary{

    public DailyConnectionModeSummary(){}

    public DailyConnectionModeSummary(long timestamp){
        super(timestamp);
    }

    public Iterator<? extends ConnectionTypeSample> getSamples(){
        String[] whereArgs = new String[1];
        whereArgs[0] =  Long.toString(getId());
        Iterator<ConnectionModeSample> samples = find(ConnectionModeSample.class, "date = ?", whereArgs, "initial_time");
        return samples;
    }

    public static DailyConnectionModeSummary getSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String[] todayWhereArgs = new String[1];
        todayWhereArgs[0] = Long.toString(calendar.getTimeInMillis());

        long count = DailyConnectionModeSummary.count(DailyConnectionModeSummary.class, "date = ?", todayWhereArgs);

        if (count < 1){
            DailyConnectionModeSummary todaySummary = new DailyConnectionModeSummary(timestamp);
            todaySummary.save();
            return todaySummary;
        }
        return DailyConnectionModeSummary.find(DailyConnectionModeSummary.class, "date = ?", todayWhereArgs, "date").next();
    }

    public long getDateMillis(){
        return date;
    }
}