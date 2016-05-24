package cl.niclabs.adkintunmobile.views.connectiontype.networktype;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.chart.StatisticInformation;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyConnectionTypeSummary;
import cl.niclabs.adkintunmobile.data.persistent.visualization.DailyNetworkTypeSummary;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.views.connectiontype.DailyConnectionTypeInformation;

/**
 * Information about Network Type Events of an specific day, according to
 * "initialTime" parameter (UNIX timestamp of that day)
 */
public class DailyNetworkTypeInformation extends DailyConnectionTypeInformation{

    public DailyNetworkTypeInformation(Context context, long initialTime, long currentTime) {
        super(context, initialTime, currentTime);
    }

    @Override
    public DailyConnectionTypeSummary getSummary(long timestamp) {
        return DailyNetworkTypeSummary.getSummary(timestamp);
    }

    @Override
    public TypedArray getConnectionTypeColors(){
        return context.getResources().obtainTypedArray(R.array.network_type_legend_colors);
    }

    @Override
    public int getPrimaryType(){
        long[] timeByType = getTimeByType();

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
    }
}
