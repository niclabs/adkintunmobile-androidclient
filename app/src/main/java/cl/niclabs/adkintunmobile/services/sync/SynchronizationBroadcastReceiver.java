package cl.niclabs.adkintunmobile.services.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.Constants;

public class SynchronizationBroadcastReceiver extends BroadcastReceiver {
    public SynchronizationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Se gatilló el receiver", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, Synchronization.class));
    }

    public void setSchedulle(Context context, long samplingMinutes){

        cancelScheculle(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        long samplingTime = samplingMinutes * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), samplingTime, pIntent);

        Toast.makeText(context, "alarma seteada en " + samplingMinutes + " minutos", Toast.LENGTH_SHORT).show();
    }

    private void cancelScheculle(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pIntent);
    }
}
