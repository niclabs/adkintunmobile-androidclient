package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionTypeSummary;

public abstract class DailyConnectionTypeInformation extends StatisticInformation{
    protected final long period = 3600L * 24L * 1000L;
    protected final float anglePerMillisecond = 360f/period;
    protected long initialTime;
    protected long currentTime;
    protected Context context;

    public DailyConnectionTypeInformation(Context context, long initialTime, long currentTime) {
        this.context = context;
        this.currentTime = currentTime;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(initialTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.initialTime = calendar.getTimeInMillis();
    }

    public abstract DailyConnectionTypeSummary getSummary(long timestamp);

    public abstract TypedArray getConnectionTypeColors();

    public abstract int getPrimaryType();

    /**
     * Get all ConnectionTypeSample's of the day represented with "initialTime" parameter.
     * Then, save ColorsArray and ValuesArray generated to build the DoughnutChart. Also save
     * TimeByType array to build Chart legend.
     */
    @Override
    public void setStatisticsInformation() {
        TypedArray connectionTypeColors = getConnectionTypeColors();

        int noInfoColor = ContextCompat.getColor(context, R.color.doughnut_no_info);
        int startColor = ContextCompat.getColor(context, R.color.doughnut_start);

        //Samples del día representado por initialTime
        DailyConnectionTypeSummary todaySummary = getSummary(initialTime);
        Iterator<? extends ConnectionTypeSample> todaySamples = todaySummary.getSamples();
        long[] timeByType = new long[connectionTypeColors.length()];

        final ArrayList<Integer> colors = new ArrayList<Integer>();
        final ArrayList<Float> values = new ArrayList<Float>();
        long lastTime;
        Integer lastColor;
        int lastType;
        ConnectionTypeSample sample;
        float initialBar = 1f;
        values.add(initialBar);
        colors.add(startColor);

        //Info del primer sample del día
        if (todaySamples.hasNext()){
            sample = todaySamples.next();
            lastTime = sample.getInitialTime();
            lastColor = connectionTypeColors.getColor(sample.getType(), 0);
            lastType = sample.getType();
        }
        else {                                      //manejo si no hay valores
            sample = null;                          //Se podría detectar acá el valor o ejecutar la sincronización
            lastColor = noInfoColor;
            lastTime = initialTime;
            lastType = -1;
        }

        //Si primer reporte del día no parte de las 0 AM, completar con último del día anterior
        if (lastTime >= initialTime) {
            DailyConnectionTypeSummary yesterdaySummary = getSummary(initialTime - period);
            Iterator<? extends ConnectionTypeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()){
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                lastColor = connectionTypeColors.getColor(sample.getType(), 0);
                colors.add( lastColor );
                lastType = sample.getType();
                timeByType[lastType] += (lastTime - initialTime);
            }
            else {
                colors.add(noInfoColor);
            }
            values.add((lastTime - initialTime) * anglePerMillisecond);
        }

        Float angle;

        //Samples del día seleccionado
        while (todaySamples.hasNext()){
            sample = todaySamples.next();
            if (lastColor == connectionTypeColors.getColor(sample.getType(), 0))
                continue;
            colors.add( lastColor );
            lastColor = connectionTypeColors.getColor(sample.getType(), 0);
            angle = (sample.getInitialTime() - lastTime) * anglePerMillisecond;
            values.add(angle);

            timeByType[lastType] += (sample.getInitialTime() - lastTime);
            lastType = sample.getType();

            lastTime = sample.getInitialTime();
        }

        //Si es un día anterior a la fecha actual
        if (currentTime >= initialTime + period){
            angle = (initialTime + period - lastTime) * anglePerMillisecond;
            values.add(angle - initialBar);
            colors.add(lastColor);
            if ((lastTime >= initialTime) && lastType!=-1)
                timeByType[lastType] += (initialTime + period - lastTime);
        }
        //Si es un día posterior a la fecha actual
        else if(initialTime > currentTime){
            colors.add(noInfoColor);
            values.add((initialTime + period - lastTime - initialBar) * anglePerMillisecond);
        }
        //Si es el día actual
        else {
            angle = (currentTime - lastTime) * anglePerMillisecond;
            values.add(angle);
            colors.add(lastColor);
            colors.add(noInfoColor);
            values.add((initialTime + period - currentTime - initialBar) * anglePerMillisecond);
            timeByType[lastType] += (currentTime - lastTime);
        }

        super.setTimeByType(timeByType);

        ArrayList<Object> results = new ArrayList<Object>();
        results.add(colors);
        results.add(values);
        super.setInformation(results);

        connectionTypeColors.recycle();
    }

    @Override
    public ArrayList<Integer> getColors() {
        return (ArrayList<Integer>) super.getInformation().get(0);
    }

    @Override
    public ArrayList<Float> getValues() {
        return (ArrayList<Float>) super.getInformation().get(1);
    }

}
