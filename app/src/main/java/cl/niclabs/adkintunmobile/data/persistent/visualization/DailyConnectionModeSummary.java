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

    /**
     * Return the time using each connection type por an specific day.
     * @param initialTime   in milliseconds (to represent an specific day)
     * @return long array with the time in milliseconds using each connection type. Index are specified in ConnectionModeSample class.
     */
    /*public static long[] getTimeByTypeSummary(long initialTime){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(initialTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        initialTime = calendar.getTimeInMillis();
        long period = 3600L * 24L * 1000L;
        DailyConnectionModeSummary todaySummary = DailyConnectionModeSummary.getSummary(initialTime);
        Iterator<? extends ConnectionTypeSample> todaySamples = todaySummary.getSamples();
        long[] timeByType = new long[3];
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
            DailyConnectionModeSummary yesterdaySummary = DailyConnectionModeSummary.getSummary(initialTime - period);
            Iterator<? extends ConnectionTypeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()){
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                lastType = sample.getType();
                timeByType[lastType] += (lastTime - initialTime);
            }
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
    }
*/
    /**
     *
     * @param
     * @return int constant from ConnectionModeSample (NONE, MOBILE, WIFI)
     */
    /*public static int getPrimaryType(long currentTime){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long[] timeByType = getTimeByTypeSummary(calendar.getTimeInMillis());

        int primaryType = ConnectionModeSample.NONE;

        if (timeByType[ConnectionModeSample.MOBILE] > timeByType[primaryType])
            primaryType = ConnectionModeSample.MOBILE;
        if (timeByType[ConnectionModeSample.WIFI] > timeByType[primaryType])
            primaryType = ConnectionModeSample.WIFI;
        return  primaryType;
    }*/

    public long getDateMillis(){
        return date;
    }
}