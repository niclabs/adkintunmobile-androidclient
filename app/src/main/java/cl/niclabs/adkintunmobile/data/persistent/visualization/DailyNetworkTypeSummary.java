package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Iterator;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class DailyNetworkTypeSummary extends DailyConnectionTypeSummary{

    public DailyNetworkTypeSummary(){}

    public DailyNetworkTypeSummary(long timestamp){
        super(timestamp);
    }

    public Iterator<? extends ConnectionTypeSample> getSamples(){
        String[] whereArgs = new String[1];
        whereArgs[0] =  Long.toString(getId());
        Iterator<NetworkTypeSample> samples = find(NetworkTypeSample.class, "date = ?", whereArgs, "initial_time");
        return samples;
    }

    public static DailyNetworkTypeSummary getSummary(long timestamp){
        String[] todayWhereArgs = new String[1];
        todayWhereArgs[0] = Long.toString(DisplayDateManager.timestampAtStartDay(timestamp));

        long count = DailyNetworkTypeSummary.count(DailyNetworkTypeSummary.class, "date = ?", todayWhereArgs);

        if (count < 1){
            DailyNetworkTypeSummary todaySummary = new DailyNetworkTypeSummary(timestamp);
            todaySummary.save();
            return todaySummary;
        }
        return DailyNetworkTypeSummary.find(DailyNetworkTypeSummary.class, "date = ?", todayWhereArgs, "date").next();
    }

    public long getDateMillis(){
        return date;
    }

}