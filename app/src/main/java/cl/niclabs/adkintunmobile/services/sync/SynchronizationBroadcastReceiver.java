package cl.niclabs.adkintunmobile.services.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SynchronizationBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "SynchronizationBR";

    public SynchronizationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, Synchronization.class));
    }

    public void setSchedule(Context context, long samplingMinutes){

        cancelSchedule(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        long samplingTime = samplingMinutes *  1000;
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), samplingTime, pIntent);
        Log.d(TAG, "alarma seteada en " + samplingMinutes + " minutos");
    }

    private void cancelSchedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pIntent);
    }
}
