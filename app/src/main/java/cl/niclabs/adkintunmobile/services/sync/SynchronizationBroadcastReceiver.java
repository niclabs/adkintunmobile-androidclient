package cl.niclabs.adkintunmobile.services.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SynchronizationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AdkM:SynchronizationBR";

    public SynchronizationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, Synchronization.class));
    }

    static public void setSchedule(Context context, long samplingMinutes){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pIntent);   // Cancel others alarms

        long samplingTime = samplingMinutes * 60 *  1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), samplingTime, pIntent);
        Log.d(TAG, "Alarma programada para " + samplingMinutes + " minutos más");
    }
}
