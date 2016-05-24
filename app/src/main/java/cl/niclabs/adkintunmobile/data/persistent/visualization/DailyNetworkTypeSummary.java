package cl.niclabs.adkintunmobile.data.persistent.visualization;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.android.data.Persistent;

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
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String[] todayWhereArgs = new String[1];
        todayWhereArgs[0] = Long.toString(calendar.getTimeInMillis());

        long count = DailyNetworkTypeSummary.count(DailyNetworkTypeSummary.class, "date = ?", todayWhereArgs);

        if (count < 1){
            DailyNetworkTypeSummary todaySummary = new DailyNetworkTypeSummary(timestamp);
            todaySummary.save();
            return todaySummary;
        }
        return DailyNetworkTypeSummary.find(DailyNetworkTypeSummary.class, "date = ?", todayWhereArgs, "date").next();
    }

    /**
     * Return the time using each network type por an specific day.
     * @param initialTime   in milliseconds (to represent an specific day)
     * @return long array with the time in milliseconds using each network type. Index are specified in NetworkTypeSample class.
     */
    /*public static long[] getTimeByTypeSummary(long initialTime) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(initialTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        initialTime = calendar.getTimeInMillis();
        long period = 3600L * 24L * 1000L;
        DailyNetworkTypeSummary todaySummary = DailyNetworkTypeSummary.getSummary(initialTime);
        Iterator<? extends ConnectionTypeSample> todaySamples = todaySummary.getSamples();
        long[] timeByType = new long[7];
        long lastTime;
        long currentTime = System.currentTimeMillis();

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
            lastTime = initialTime;
        }

        //Si primer reporte del día no parte de las 0 AM, completar con último del día anterior
        if (lastTime >= initialTime) {
            DailyNetworkTypeSummary yesterdaySummary = DailyNetworkTypeSummary.getSummary(initialTime - period);
            Iterator<? extends ConnectionTypeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()) {
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                lastType = sample.getType();
                timeByType[lastType] += (lastTime - initialTime);
            }
        }

        //Samples del día
        while (todaySamples.hasNext()){
            sample = todaySamples.next();
            if (lastType == sample.getType())
                continue;
            timeByType[lastType] += (sample.getInitialTime() - lastTime);
            lastType = sample.getType();
            lastTime = sample.getInitialTime();
        }

        //Si es un día anterior a la fecha
        if (currentTime >= initialTime + period){
            if (lastTime > initialTime)
                timeByType[lastType] += (initialTime + period - lastTime);
        }
        //Si es un día posterior a la fecha actual
        else if(initialTime > currentTime){
        }
        //Si es el día actual
        else {
            timeByType[lastType] += (currentTime - lastTime);
        }
        return timeByType;
    }*/

    /**
     *
     * @param
     * @return int constant from ConnectionModeSample (UNKNOWN, TYPE_G, TYPE_E, TYPE_3G, TYPE_H, TYPE_Hp, TYPE_4G)
     */
/*    public static int getPrimaryType(long currentTime){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long[] timeByType = getTimeByTypeSummary(calendar.getTimeInMillis());

        int primaryType = NetworkTypeSample.UNKNOWN;

        if (timeByType[NetworkTypeSample.TYPE_G] > timeByType[primaryType])
            primaryType = NetworkTypeSample.TYPE_G;
        if (timeByType[NetworkTypeSample.TYPE_E] > timeByType[primaryType])
            primaryType = NetworkTypeSample.TYPE_E;
        if (timeByType[NetworkTypeSample.TYPE_3G] > timeByType[primaryType])
            primaryType = NetworkTypeSample.TYPE_3G;
        if (timeByType[NetworkTypeSample.TYPE_H] > timeByType[primaryType])
            primaryType = NetworkTypeSample.TYPE_H;
        if (timeByType[NetworkTypeSample.TYPE_Hp] > timeByType[primaryType])
            primaryType = NetworkTypeSample.TYPE_Hp;
        if (timeByType[NetworkTypeSample.TYPE_4G] > timeByType[primaryType])
            primaryType = NetworkTypeSample.TYPE_4G;
        return  primaryType;
    }*/

    public long getDateMillis(){
        return date;
    }

}