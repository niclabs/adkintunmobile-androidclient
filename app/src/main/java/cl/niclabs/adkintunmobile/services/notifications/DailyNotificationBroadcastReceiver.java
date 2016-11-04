package cl.niclabs.adkintunmobile.services.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.display.NotificationManager;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.applicationstraffic.ApplicationsTrafficActivity;

public class DailyNotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AdkM:DailyNotifBR";


    public DailyNotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        long[] dailyData = ApplicationTraffic.getTransferedData(ApplicationTraffic.MOBILE, DisplayDateManager.timestampAtStartDay(System.currentTimeMillis()));
        String dataUsage = Network.formatBytes(dailyData[0] + dailyData[1]);
        String date = DisplayDateManager.getDateString(System.currentTimeMillis(), new SimpleDateFormat("dd/MM"));
        String title = context.getString(R.string.notification_daily_report_title);
        String body = String.format(context.getString(R.string.notification_daily_report_body) , date, dataUsage);

        NewsNotification notification = new NewsNotification(NewsNotification.INFO, title, body);
        notification.save();

        NewsNotification n = NewsNotification.findFirst(
                NewsNotification.class,
                "timestamp = ?",
                NewsNotification.mostRecentlyTimestamp()+"");
        NotificationManager.showNotification(context, n.title, n.content, new Intent(context, ApplicationsTrafficActivity.class));
    }

    static public void setSchedule(Context context){

        cancelSchedule(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(context, DailyNotificationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pIntent);
        Log.d(TAG, "Alarma programada");
    }

    public static void cancelSchedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyNotificationBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pIntent);   // Cancel others alarms
        Log.d(TAG, "Alarma cancelada");
    }
}
