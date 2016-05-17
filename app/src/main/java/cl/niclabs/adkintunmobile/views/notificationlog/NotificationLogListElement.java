package cl.niclabs.adkintunmobile.views.notificationlog;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class NotificationLogListElement {

    private String title;
    private String content;
    private long timestamp;
    private boolean mark;

    public NotificationLogListElement(String title, String content, long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.mark = true;
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
        return DisplayDateManager.getDateString(this.getTimestamp());
    }
}
