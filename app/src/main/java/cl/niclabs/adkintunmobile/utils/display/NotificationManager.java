package cl.niclabs.adkintunmobile.utils.display;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import cl.niclabs.adkintunmobile.R;

public class NotificationManager {

    static public final int adkIDNotifNum = 5551234;
    static public final long[] vibratePattern = {100L,100L,100L,100L,100L,500L,100L,500L,100L};

    static public void showNotification(Context context, String titulo, String mensaje){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setLargeIcon(((BitmapDrawable)ContextCompat.getDrawable(context, R.mipmap.icon)).getBitmap())
                    .setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(context, R.color.doughnut_mobile))
                    .setVibrate(vibratePattern)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        android.app.Notification mNotification = mBuilder.build();
        mNotificationManager.notify(adkIDNotifNum, mNotification);
    }
}
