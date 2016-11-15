package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Iterator;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

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
        String[] todayWhereArgs = new String[1];
        todayWhereArgs[0] = Long.toString(DisplayDateManager.timestampAtStartDay(timestamp));

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