package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTimeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectedTimeSummary;
import cl.niclabs.adkintunmobile.services.sync.Synchronization;

public class DailyConnectionTypeInformation extends StatisticInformation {

    private final long period = 3600L * 24L * 1000L;
    private final float anglePerMillisecond = 360f/period;
    private long timestamp;
    private long currentTime;
    private Context context;


    public DailyConnectionTypeInformation(Context context, long timestamp, long currentTime) {
        this.context = context;
        this.timestamp = timestamp;
        this.currentTime = currentTime;
    }

    @Override
    public void setStatisticsInformation() {
        int mobileColor = ContextCompat.getColor(context, R.color.doughnut_mobile);
        int disconnectedColor = ContextCompat.getColor(context, R.color.doughnut_no_connection);
        int wifiColor = ContextCompat.getColor(context, R.color.doughnut_wifi);
        int[] connectionTypeColors = {disconnectedColor, mobileColor, wifiColor};
        int noInfoColor = ContextCompat.getColor(context, R.color.doughnut_no_info);
        int startColor = ContextCompat.getColor(context, R.color.doughnut_start);

        DailyConnectedTimeSummary todaySummary = DailyConnectedTimeSummary.getSummary(timestamp);


        Iterator<ConnectionTimeSample> todaySamples = todaySummary.getSamples();
        final ArrayList<Integer> colors = new ArrayList<Integer>();
        final ArrayList<Float> values = new ArrayList<Float>();
        long lastTime;
        Integer lastColor;
        ConnectionTimeSample sample;
        long accumulatedTime;

        values.add(2f);
        colors.add(startColor);


        if (todaySamples.hasNext()){
            sample = todaySamples.next();
            lastTime = sample.getInitialTime();
            lastColor = connectionTypeColors[sample.getType()];

        }
        else {                                      //manejo si no hay valores
            sample = null;                          //Se podría detectar acá el valor o ejecutar la sincronización
            lastColor = noInfoColor;
            lastTime = todaySummary.getDateMillis();
        }

        //Si Primer reporte del día no parte de las 0 AM
        if (lastTime > todaySummary.getDateMillis()) {
            DailyConnectedTimeSummary yesterdaySummary = DailyConnectedTimeSummary.getSummary(timestamp - period);
            Iterator<ConnectionTimeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()){
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                colors.add( connectionTypeColors[sample.getType()] );
            }
            else {
                colors.add(noInfoColor);
            }
            Log.d("Color", lastTime + " " + todaySummary.getDateMillis());
            values.add((lastTime - todaySummary.getDateMillis()) * anglePerMillisecond);
        }

        accumulatedTime = lastTime;
        Float angle;
        while (todaySamples.hasNext()){
            sample = todaySamples.next();
            if (lastColor == connectionTypeColors[sample.getType()])
                continue;
            colors.add( lastColor );
            lastColor = connectionTypeColors[sample.getType()];
            angle = (sample.getInitialTime() - lastTime) * anglePerMillisecond;
            values.add(angle);
            lastTime = sample.getInitialTime();
        }
        angle = (timestamp - lastTime) * anglePerMillisecond;
        values.add(angle);
        colors.add(lastColor);
        accumulatedTime = timestamp - accumulatedTime;
        colors.add(noInfoColor);
        values.add((todaySummary.getDateMillis() + period - timestamp)*anglePerMillisecond);
        ArrayList<Object> results = new ArrayList<Object>();
        results.add(colors);
        results.add(values);
        super.setInformation(results);
    }

    @Override
    public ArrayList<Integer> getColors() {
        return (ArrayList<Integer>) super.getInformation().get(0);
    }

    @Override
    public ArrayList<Float> getValues() {
        return (ArrayList<Float>) super.getInformation().get(1);
    }

    @Override
    public ArrayAdapter<Integer> getAdapter() {
        return null;
    }

}
