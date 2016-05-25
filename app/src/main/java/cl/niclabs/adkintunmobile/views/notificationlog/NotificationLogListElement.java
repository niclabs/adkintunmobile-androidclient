package cl.niclabs.adkintunmobile.views.notificationlog;

import java.text.SimpleDateFormat;

import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class NotificationLogListElement {

    private String title;
    private String content;
    private long timestamp;
    private boolean mark;

    /*
    public NotificationLogListElement(String title, String content, long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.mark = true;
    }
    */

    public NotificationLogListElement(NewsNotification newsNotification){
        this.title = newsNotification.title;
        this.content = newsNotification.content;
        this.timestamp = newsNotification.timestamp;
        this.mark = newsNotification.unRead;
    }

    public String getTitle() {
        return title;
    }

    public boolean isMark() {
        return mark;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getDate(){
        return DisplayDateManager.getDateString(this.getTimestamp(), new SimpleDateFormat("dd/MM/yyyy HH:mm"));
    }
}
