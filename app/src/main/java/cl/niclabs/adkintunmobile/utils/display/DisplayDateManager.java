package cl.niclabs.adkintunmobile.utils.display;

import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cl.niclabs.adkintunmobile.R;

public class DisplayDateManager {
    String[] dayOfWeek;
    String[] monthOfYear;

    public DisplayDateManager(Context context){
        dayOfWeek = context.getResources().getStringArray(R.array.day_of_week);
        monthOfYear = context.getResources().getStringArray(R.array.month_of_year);
    }

    public void setDayOfWeek(TextView dayText, Calendar calendar){
        dayText.setText(dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
    }

    public void setDate(TextView dateText, Calendar calendar){
        StringBuilder sb = new StringBuilder(5);
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append("-");
        sb.append(monthOfYear[calendar.get(Calendar.MONTH)]);
        sb.append("-");
        sb.append(calendar.get(Calendar.YEAR));

        dateText.setText(sb.toString());
    }

    public void refreshDate(TextView dayText, TextView dateText, long initialTime){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(initialTime);
        setDayOfWeek(dayText, calendar);
        setDate(dateText, calendar);
    }

    static public String getDateString(long timestamp, SimpleDateFormat customFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return customFormat.format(calendar.getTime());
    }

    static public String getDateString(long timestamp){
        return DisplayDateManager.getDateString(timestamp, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
    }

    static public int daysBetweenTimestamps(long t1, long t2){
        long diff = Math.abs(t1-t2);
        return (int)((((diff/1000)/60)/60)/24);
    }

    static public long timestampAtStartDay(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}