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

    public static int[] getTimeByTypeSummary(long currentTime){
        DailyConnectionTypeSummary todaySummary = DailyConnectionTypeSummary.getSummary(currentTime);
        Iterator<ConnectionTypeSample> todaySamples = todaySummary.getSamples();
        int[] timeByType = new int[3];
        long lastTime;

        int lastType;
        ConnectionTypeSample sample;

        //Info del primer sample del día
        if (todaySamples.hasNext()){
            sample = todaySamples.next();
            lastTime = sample.getInitialTime();
            lastType = sample.getType();
        }
        else {                                      //manejo si no hay valores
            sample = null;                          //Se podría detectar acá el valor o ejecutar la sincronización
            lastType = 0;
            lastTime = currentTime;
        }

        //Si primer reporte del día no parte de las 0 AM, completar con último del día anterior
        if (lastTime > currentTime) {
            DailyConnectionTypeSummary yesterdaySummary = DailyConnectionTypeSummary.getSummary(currentTime - 3600L * 24L * 1000L);
            Iterator<ConnectionTypeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()){
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                lastType = sample.getType();
            }
            else {
                lastType = 0;
            }
            timeByType[lastType] += (lastTime - currentTime);
        }

        //Samples del día seleccionado
        while (todaySamples.hasNext()){
            sample = todaySamples.next();
            if (lastType == sample.getType())
                continue;
            timeByType[lastType] += (sample.getInitialTime() - lastTime);
            lastType = sample.getType();
            lastTime = sample.getInitialTime();
        }
        timeByType[lastType] += (currentTime - lastTime);

        return timeByType;
    }

    public long getDateMillis(){
        return date;
    }
}