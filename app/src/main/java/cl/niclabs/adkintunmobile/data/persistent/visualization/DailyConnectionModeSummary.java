package cl.niclabs.adkintunmobile.data.persistent.visualization;

import android.util.Log;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.android.data.Persistent;

public class DailyConnectionModeSummary extends Persistent<DailyConnectionModeSummary> {

    public long date;

    public DailyConnectionModeSummary(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTimeInMillis();

    }


    public DailyConnectionModeSummary(){}

    public Iterator<ConnectionModeSample> getSamples(){
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

    /**
     * Return the time using each connection type por an specific day.
     * @param currentTime   in milliseconds (to represent an specific day)
     * @return long array with the time in milliseconds using each connection type. Index are specified in ConnectionModeSample class.
     */
    public static long[] getTimeByTypeSummary(long currentTime){
        long period = 3600L * 24L * 1000L;
        DailyConnectionModeSummary todaySummary = DailyConnectionModeSummary.getSummary(currentTime);
        Iterator<ConnectionModeSample> todaySamples = todaySummary.getSamples();
        long[] timeByType = new long[3];
        long lastTime;

        int lastType;
        ConnectionModeSample sample;

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
            DailyConnectionModeSummary yesterdaySummary = DailyConnectionModeSummary.getSummary(currentTime - period);
            Iterator<ConnectionModeSample> yesterdaySamples = yesterdaySummary.getSamples();
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

    public static String getPrimaryType(long currentTime){
        long[] timeByType = getTimeByTypeSummary(currentTime);
        String primaryType = "Sin Conexión";
        Log.d("AAAA", timeByType[0]+" "+timeByType[1]+" "+timeByType[2]);
        if (timeByType[1]>timeByType[0])
            primaryType = "Mobile";
        if (timeByType[2]>timeByType[1])
            primaryType = "Wi-Fi";
        return  primaryType;
    }

    public long getDateMillis(){
        return date;
    }
}