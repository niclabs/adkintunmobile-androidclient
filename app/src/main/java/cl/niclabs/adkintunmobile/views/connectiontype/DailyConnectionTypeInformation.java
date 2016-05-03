package cl.niclabs.adkintunmobile.views.connectiontype;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionTypeSummary;

/**
 * Information about Connection Type Events of an specific day, according to
 * "initialTime" parameter (UNIX timestamp of that day)
 */
public class DailyConnectionTypeInformation extends StatisticInformation {

    private final long period = 3600L * 24L * 1000L;
    private final float anglePerMillisecond = 360f/period;
    private long initialTime;
    private long currentTime;
    private Context context;

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

    /**
     * Get all ConnectionTypeSample's of the day represented with "initialTime" parameter.
     * Then, save ColorsArray and ValuesArray generated to build the DoughnutChart.
     */
    @Override
    public void setStatisticsInformation() {
        int mobileColor = ContextCompat.getColor(context, R.color.doughnut_mobile);
        int disconnectedColor = ContextCompat.getColor(context, R.color.doughnut_no_connection);
        int wifiColor = ContextCompat.getColor(context, R.color.doughnut_wifi);

        int[] connectionTypeColors = {disconnectedColor, mobileColor, wifiColor};

        int noInfoColor = ContextCompat.getColor(context, R.color.doughnut_no_info);
        int startColor = ContextCompat.getColor(context, R.color.doughnut_start);

        //Samples del día representado por initialTime
        DailyConnectionTypeSummary todaySummary = DailyConnectionTypeSummary.getSummary(initialTime);
        Iterator<ConnectionTypeSample> todaySamples = todaySummary.getSamples();

        final ArrayList<Integer> colors = new ArrayList<Integer>();
        final ArrayList<Float> values = new ArrayList<Float>();
        long lastTime;
        Integer lastColor;
        ConnectionTypeSample sample;
        float initialBar = 1f;
        values.add(initialBar);
        colors.add(startColor);

        //Info del primer sample del día
        if (todaySamples.hasNext()){
            sample = todaySamples.next();
            lastTime = sample.getInitialTime();
            lastColor = connectionTypeColors[sample.getType()];

        }
        else {                                      //manejo si no hay valores
            sample = null;                          //Se podría detectar acá el valor o ejecutar la sincronización
            lastColor = noInfoColor;
            lastTime = initialTime;

        }

        //Si primer reporte del día no parte de las 0 AM, completar con último del día anterior
        if (lastTime > initialTime) {
            DailyConnectionTypeSummary yesterdaySummary = DailyConnectionTypeSummary.getSummary(initialTime - period);
            Iterator<ConnectionTypeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()){
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                colors.add( connectionTypeColors[sample.getType()] );
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
            if (lastColor == connectionTypeColors[sample.getType()])
                continue;
            colors.add( lastColor );
            lastColor = connectionTypeColors[sample.getType()];
            angle = (sample.getInitialTime() - lastTime) * anglePerMillisecond;
            values.add(angle);
            lastTime = sample.getInitialTime();
        }

        //Si es un día anterior a la fecha actual
        if (currentTime >= initialTime + period){
            angle = (initialTime + period - lastTime) * anglePerMillisecond;
            values.add(angle - initialBar);
            colors.add(lastColor);
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
        }

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
