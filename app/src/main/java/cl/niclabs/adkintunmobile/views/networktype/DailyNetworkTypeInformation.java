package cl.niclabs.adkintunmobile.views.networktype;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyNetworkTypeSummary;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;

public class DailyNetworkTypeInformation extends StatisticInformation{
    private final long period = 3600L * 24L * 1000L;
    private final float anglePerMillisecond = 360f/period;
    private long initialTime;
    private long currentTime;
    private Context context;


    public DailyNetworkTypeInformation(Context context, long initialTime, long currentTime) {
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

    @Override
    public void setStatisticsInformation() {
        int unknownColor = ContextCompat.getColor(context, R.color.network_type_unknown);
        int NetworkTypeGColor = ContextCompat.getColor(context, R.color.network_type_G);
        int NetworkTypeEColor = ContextCompat.getColor(context, R.color.network_type_E);
        int NetworkType3GColor = ContextCompat.getColor(context, R.color.network_type_3G);
        int NetworkTypeHColor = ContextCompat.getColor(context, R.color.network_type_H);
        int NetworkTypeHPColor = ContextCompat.getColor(context, R.color.network_type_Hp);
        int NetworkType4GColor = ContextCompat.getColor(context, R.color.network_type_4G);

        int[] networkTypeColors = {unknownColor, NetworkTypeGColor, NetworkTypeEColor,
                NetworkType3GColor, NetworkTypeHColor, NetworkTypeHPColor, NetworkType4GColor};

        int noInfoColor = ContextCompat.getColor(context, R.color.doughnut_no_info);
        int startColor = ContextCompat.getColor(context, R.color.doughnut_start);

        DailyNetworkTypeSummary todaySummary = DailyNetworkTypeSummary.getSummary(initialTime);


        Iterator<NetworkTypeSample> todaySamples = todaySummary.getSamples();
        final ArrayList<Integer> colors = new ArrayList<Integer>();
        final ArrayList<Float> values = new ArrayList<Float>();
        long lastTime;
        Integer lastColor;
        NetworkTypeSample sample;
        float initialBar = 1f;
        values.add(initialBar);
        colors.add(startColor);

        //Info del primer sample del día
        if (todaySamples.hasNext()){
            sample = todaySamples.next();
            lastTime = sample.getInitialTime();
            lastColor = networkTypeColors[sample.getType()];

        }
        else {                                      //manejo si no hay valores
            sample = null;                          //Se podría detectar acá el valor o ejecutar la sincronización
            lastColor = noInfoColor;
            lastTime = initialTime;

        }

        //Si primer reporte del día no parte de las 0 AM, buscar último del día anterior
        if (lastTime > initialTime) {
            DailyNetworkTypeSummary yesterdaySummary = DailyNetworkTypeSummary.getSummary(initialTime - period);
            Iterator<NetworkTypeSample> yesterdaySamples = yesterdaySummary.getSamples();
            if (yesterdaySamples.hasNext()){
                while (yesterdaySamples.hasNext()) {
                    sample = yesterdaySamples.next();
                }
                colors.add( networkTypeColors[sample.getType()] );
            }
            else {
                colors.add(noInfoColor);
            }
            values.add((lastTime - initialTime) * anglePerMillisecond);
        }

        Float angle;

        //Samples del día
        while (todaySamples.hasNext()){
            sample = todaySamples.next();
            if (lastColor == networkTypeColors[sample.getType()])
                continue;
            colors.add( lastColor );
            lastColor = networkTypeColors[sample.getType()];
            angle = (sample.getInitialTime() - lastTime) * anglePerMillisecond;
            values.add(angle);
            lastTime = sample.getInitialTime();
        }

        //Si es un día anterior a la fecha
        if (currentTime >= initialTime + period){
            angle = (initialTime + period - lastTime) * anglePerMillisecond;
            values.add(angle - initialBar);
            colors.add(lastColor);
        }
        //Si es un día posterior a la fecha
        else if(initialTime > currentTime){
            colors.add(noInfoColor);
            values.add((initialTime + period - lastTime - initialBar) * anglePerMillisecond);
        }
        //Día actual
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
