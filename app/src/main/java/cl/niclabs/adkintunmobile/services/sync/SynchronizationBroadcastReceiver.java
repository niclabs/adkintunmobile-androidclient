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

    public void setSchedulle(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //am.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pIntent);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), Constants.MILIS_REPEATING_SYNC, pIntent);
    }
}
